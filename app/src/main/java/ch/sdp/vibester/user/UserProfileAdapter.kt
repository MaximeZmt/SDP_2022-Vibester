package ch.sdp.vibester.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.helper.loadImg

/**
 * UserAdapter to set userProfile views with username and image in RecycleView. It is used to search for users.
 */
class UserProfileAdapter constructor(val users: MutableList<User>, val testVisibility: Boolean = false):
    RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    val authenticator: FireBaseAuthenticator = FireBaseAuthenticator()
    val usersRepo: UsersRepo = UsersRepo()
    /**
     * Create a RecycleView layout with the userProfile view as an item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_search_item_layout, parent, false)
        return UserProfileViewHolder(view)
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
    inner class UserProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * @param user with all the parameters
         */
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.search_user_username).text = user.username
            itemView.findViewById<ImageView>(R.id.profile_image).loadImg(user.image)

            val addFriendBtn = itemView.findViewById<Button>(R.id.addFriendBtn)
            addFriendBtn.setOnClickListener{
                val currentUser = authenticator.getCurrUser()
                if(currentUser != null || testVisibility){
                    if(!testVisibility)usersRepo.updateFieldSubFieldBoolean(currentUser!!.uid, true, "friends", user.uid)
                    addFriendBtn.visibility = View.INVISIBLE
                    itemView.findViewById<ImageView>(R.id.addedFriendIcon).visibility = View.VISIBLE
                }
            }
        }

    }


}