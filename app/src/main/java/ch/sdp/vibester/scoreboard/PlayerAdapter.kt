package ch.sdp.vibester.scoreboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R

class PlayerAdapter(playersInit: List<Player>) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    var players: MutableList<Player> = playersInit.toMutableList()

    override fun getItemCount(): Int = players.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scoreboard_item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position], position)
    }

    fun addPlayers(players: List<Player>) {
        this.players.apply {
            clear()
            addAll(players)
        }
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: Player, position: Int) {
            val newPosition = position + 1
            itemView.findViewById<TextView>(R.id.tv_position).text = (newPosition).toString()
            itemView.findViewById<TextView>(R.id.tv_name).text = player.name
            itemView.findViewById<TextView>(R.id.tv_score).text = player.score.toString()
            itemView.findViewById<ImageView>(R.id.iv_photo).loadImg(player.photo)
        }
    }
}