package ch.sdp.vibester.user

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.helper.ImageHelper


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
    private var userFollowing: Array<String> = arrayOf()
    private val imageSize = 100

    init {
        if (currentUser != null) { dataGetter.getUserData(currentUser.uid, this::setFriends) }
    }

    // Callback for getUserData
    private fun setFriends(user:User){
        userFollowing = user.following.keys.toTypedArray()
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
    override fun getItemCount() = users.size



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
            val addFriendBtn = itemView.findViewById<Button>(R.id.addFollowingBtn)

            if (userFollowing.isNotEmpty() && user.uid in userFollowing) {
                changeBtnToImage()
            }
            else {
                addFriendBtn.setOnClickListener {
                    if (currentUser != null) {
                        dataGetter.setSubFieldValue(currentUser.uid, "following", user.uid,true)
                        changeBtnToImage()
                    }
                }
            }
        }

        private fun setImage(imageURI: Uri) {
            val avatar = itemView.findViewById<ImageView>(R.id.search_user_profile_image)

            ImageHelper().setImage(imageURI, avatar, imageSize)
        }


        private fun changeBtnToImage() {
            AdapterHelper().changeBtnToImageHelper(R.id.addFollowingBtn, R.id.addedFollowingIcon, itemView)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}