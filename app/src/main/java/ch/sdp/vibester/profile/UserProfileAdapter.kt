package ch.sdp.vibester.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R

/**
 * UserAdapter to set userProfile views with username and image in RecycleView. It is used to search for users.
 */
class UserProfileAdapter(val users: MutableList<UserProfile>):
    RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

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
         * @param userProfile with all the parameters
         */
        fun bind(user: UserProfile) {
            itemView.findViewById<TextView>(R.id.search_user_username).text = user.username
//                TODO fix the image upload
//            itemView.findViewById<ImageView>(R.id.iv_photo).loadImg(player.photo)
        }
    }


}