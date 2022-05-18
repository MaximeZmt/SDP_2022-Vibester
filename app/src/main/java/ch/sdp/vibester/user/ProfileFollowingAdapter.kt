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
 * Set friends in userProfile with profile photo and username in RecycleView.
 */
class ProfileFollowingAdapter constructor(
    private val following: MutableList<User>,
    private val listener: OnItemClickListener?
): RecyclerView.Adapter<ProfileFollowingAdapter.ProfileFollowingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFollowingViewHolder {
        return ProfileFollowingViewHolder(
            AdapterHelper().createViewForViewHolder(parent, R.layout.profile_following_item)
        )
    }

    override fun onBindViewHolder(holder: ProfileFollowingViewHolder, position: Int) {
        holder.bind(following[position])
    }

    override fun getItemCount() = following.size


    inner class ProfileFollowingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        /**
         * bind the view with information given by user
         * @param user with all the parameters
         */
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.profile_following_username).text = user.username
            itemView.findViewById<ImageView>(R.id.profile_following_profile_image).loadImg(user.image)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}