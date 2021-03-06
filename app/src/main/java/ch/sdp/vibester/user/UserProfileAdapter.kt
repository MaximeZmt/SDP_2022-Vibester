package ch.sdp.vibester.user

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.helper.Helper


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
    private var userFollowing: MutableList<String> = mutableListOf()
    private val imageSize = 100

    init {
        if (currentUser != null) { dataGetter.getUserData(currentUser.uid, this::setFollowing) }
    }

    // Callback for getUserData
    private fun setFollowing(user:User) {
        user.following.forEach { (userId, isFollowing) -> if (isFollowing) userFollowing.add(userId) }
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
        holder.bind(users[position], position)
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
        fun bind(user: User, position: Int) {
            itemView.findViewById<TextView>(R.id.search_user_username).text = user.username
            if(position %2 == 0) itemView.setBackgroundColor(itemView.resources.getColor(R.color.darker_floral_white))

            imageGetter.fetchImage("profileImg/${user.uid}", this::setImage)

            setFollowBtnListener(user)
            unFollowBtnListener(user)
        }

        private fun setImage(imageURI: Uri) {
            val avatar = itemView.findViewById<ImageView>(R.id.search_user_profile_image)

            Helper().setImage(imageURI, avatar, imageSize)
        }

        private fun setFollowBtnListener(user: User) {
            val followBtn = itemView.findViewById<ImageView>(R.id.search_user_add)

            if (userFollowing.isNotEmpty() && user.uid in userFollowing) {
                changeBtnToImage()
            }
            else {
                followBtn.setOnClickListener {
                    if (currentUser != null) {
                        dataGetter.setFollowing(currentUser.uid, user.uid)
                        changeBtnToImage()
                    } else {
                        val text = R.string.searchUser_pleaseLogInFirst
                        Toast.makeText(it.context, text, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        private fun changeBtnToImage() {
            AdapterHelper().changeAToB(
                R.id.search_user_add, R.id.search_user_added, itemView
            )
        }

        private fun unFollowBtnListener(user: User) {
            itemView.findViewById<ImageView>(R.id.search_user_added).setOnClickListener {
                if (currentUser != null) {
                    dataGetter.setUnfollow(currentUser.uid, user.uid)
                    AdapterHelper().switchViewsVisibility(
                        itemView.findViewById(R.id.search_user_added), itemView.findViewById(R.id.search_user_add))
                }
            }
        }


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}