package ch.sdp.vibester.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R

/**
 * UserAdapter that is used in the RecycleView to search users
 */
class UserProfileAdapter(val users: MutableList<UserProfile>):
    RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_search_item_layout, parent, false)
        return UserProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return  users.size
    }

    /**
     * Customer ViewHolder class for UserProfile
     */
    inner class UserProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * @param userProfile with all the parameters
         */
        fun bind(user: UserProfile) {
            itemView.findViewById<TextView>(R.id.username).text = user.username
//            itemView.findViewById<ImageView>(R.id.iv_photo).loadImg(player.photo) TODO fix the image upload
        }
    }

    /**
     * Functions for testing
     */

    fun addUsers(users: List<UserProfile>) {
        this.users.apply {
            clear()
            addAll(users)
        }
        notifyDataSetChanged()
    }


}