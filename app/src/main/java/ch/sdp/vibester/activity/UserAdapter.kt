package ch.sdp.vibester.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.profile.UserProfile

/**
 * UserAdapter that is used in the RecycleView to search users
 */
class UserAdapter(
    private val mContext: Context,
    private val mUsers: List<UserProfile>,
): RecyclerView.Adapter<UserAdapter.UserProfileViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, parent, false)
        return UserProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  mUsers.size
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
//            itemView.findViewById<ImageView>(R.id.iv_photo).loadImg(player.photo)
        }
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.bind(mUsers[position])
    }

}