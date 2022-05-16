package ch.sdp.vibester.user

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.helper.loadImg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * UserAdapter to set userProfile views with username and image in RecycleView. It is used to search for users.
 */
class UserProfileAdapter constructor(
    private val users: MutableList<User>,
    authenticator: FireBaseAuthenticator,
    val dataGetter: DataGetter,
    val imageGetter: ImageGetter,
    private val listener: OnItemClickListener?
    ):
    RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    private val currentUser = authenticator.getCurrUser()
    private var userFriends: Array<String> = arrayOf()
    private val imageSize = 100

    init {
        if (currentUser != null) { dataGetter.getUserData(currentUser.uid, this::setFriends) }
    }

    // Callback for getUserData
    private fun setFriends(user:User){
        userFriends = user.friends.keys.toTypedArray()
    }

    /**
     * Update users in the search list
     * @param new_users to set in search list
     */
    fun updateUsersList(new_users: MutableList<User>) {
        this.users.clear()
        this.users.addAll(new_users)
    }

    /**
     * Create a RecycleView layout with the userProfile view as an item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        return UserProfileViewHolder(
            AdapterHelper().createViewForViewHolder(parent, R.layout.user_search_item_layout)
        )
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(users[position])
    }

    /**
     * Get amount of users displayed
     */
    override fun getItemCount(): Int {
        return users.size
    }



    /**
     * Customer ViewHolder class for UserProfile. Each item contains username and image.
     */
    inner class UserProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        /**
         * @param user with all the parameters
         */
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.search_user_username).text = user.username

            imageGetter.fetchImage("profileImg/${user.uid}", this::setImage)
            //itemView.findViewById<ImageView>(R.id.profile_image).loadImg(user.image)
            val addFriendBtn = itemView.findViewById<Button>(R.id.addFriendBtn)

            if (userFriends.isNotEmpty() && user.uid in userFriends) {
                changeBtnToImage()
            }
            else {
                addFriendBtn.setOnClickListener {
                    if (currentUser != null) {
                        dataGetter.setSubFieldValue(currentUser.uid, "friends", user.uid,true)
                        changeBtnToImage()
                    }
                }
            }
        }

        private fun setImage(imageURI: Uri) {
            CoroutineScope(Dispatchers.Main).launch {
                val task = async(Dispatchers.IO) {
                    try {
                        val bit = BitmapGetterApi.download(imageURI.toString())
                        bit.get(10, TimeUnit.SECONDS)
                    } catch (e: Exception){
                        null
                    }
                }
                val bm = task.await()

                if (bm != null) {
                    val avatar = itemView.findViewById<ImageView>(R.id.profile_image)
                    avatar.setImageBitmap(Bitmap.createScaledBitmap(bm, imageSize, imageSize, false))
                }
            }
        }


        private fun changeBtnToImage() {
            AdapterHelper().changeBtnToImageHelper(R.id.addFriendBtn, R.id.addedFriendIcon, itemView)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}