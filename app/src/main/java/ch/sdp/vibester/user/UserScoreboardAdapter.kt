package ch.sdp.vibester.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.helper.loadImg

/**
 * Set users list in recycleView in scoreboard
 */
class UserScoreboardAdapter(
    playersInit: MutableList<User>, private val genre: String,
    private val listener: OnItemClickListener?
): RecyclerView.Adapter<UserScoreboardAdapter.PlayerViewHolder>() {

    var players: MutableList<User> = playersInit

    override fun getItemCount(): Int = players.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scoreboard_item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position], position)
    }

    fun addPlayers(players: List<User>?) {
        this.players.apply {
            clear()
            if (players != null) {
                addAll(players)
            }
        }
        notifyDataSetChanged()
    }

    private fun setScore(player: User): Int {
        return player.scores.getOrDefault(genre, 0)
    }

    /**
     * Customer ViewHolder class for PlayerAdapter
     */
    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        /**
         * @param player
         * @param position position of the player
         */
        fun bind(player: User, position: Int) {
            val newPosition = position + 1
            itemView.findViewById<TextView>(R.id.tv_position).text = (newPosition).toString()
            itemView.findViewById<TextView>(R.id.tv_name).text = player.username
            itemView.findViewById<TextView>(R.id.tv_score).text = setScore(player).toString()
            itemView.findViewById<ImageView>(R.id.iv_photo).loadImg(player.image)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }
    }
}