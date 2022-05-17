package ch.sdp.vibester.user

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.helper.loadImg

/**
 * set friends in userProfile with profile photo and username in RecycleView.
 */
class ProfileFriendsAdapter constructor(
    private val friends: MutableList<User>,
    private val listener: OnItemClickListener?
): RecyclerView.Adapter<ProfileFriendsAdapter.ProfileFriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFriendsViewHolder {
        return ProfileFriendsViewHolder(
            AdapterHelper().createViewForViewHolder(parent, R.layout.profile_friends_item)
        )
    }

    override fun onBindViewHolder(holder: ProfileFriendsViewHolder, position: Int) {
        holder.bind(friends[position])
    }

    override fun getItemCount() = friends.size


    inner class ProfileFriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        /**
         * @param user with all the parameters
         */
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.profile_friends_username).text = user.username
            itemView.findViewById<ImageView>(R.id.profile_friends_profile_image).loadImg(user.image)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}