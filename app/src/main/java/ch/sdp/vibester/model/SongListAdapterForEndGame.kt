package ch.sdp.vibester.model

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.user.OnItemClickListener


/**
 * SongListAdapter to set correct/wrong guessed songs in the game.
 */
class SongListAdapterForEndGame constructor(
    private val incorrectSongList: ArrayList<String>,
    correctSongList: ArrayList<String>,
    private val listener: OnItemClickListener?
) : RecyclerView.Adapter<SongListAdapterForEndGame.SongListForEndGameViewHolder>() {
    private val songList: ArrayList<String> = arrayListOf()

    init {
        songList.addAll(incorrectSongList)
        songList.addAll(correctSongList)
    }

    /**
     * Create a RecycleView layout with the Song view as an item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListForEndGameViewHolder {
        return SongListForEndGameViewHolder(
            AdapterHelper().createViewForViewHolder(parent, R.layout.song_item_download_layout)
        )
    }

    override fun onBindViewHolder(holder: SongListForEndGameViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    /**
     * Get amount of songs displayed
     */
    override fun getItemCount() = songList.size


    /**
     * Customer ViewHolder class for SongList. Each item contains the name of the song and a button.
     */
    inner class SongListForEndGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bind(songName: String) {
            itemView.findViewById<TextView>(R.id.download_song_name).text = songName
            itemView.findViewById<Button>(R.id.song_download).setOnClickListener(this)

            // Make background red if song is guessed incorrectly
            if (songName in incorrectSongList) {
                itemView.setBackgroundResource(R.color.light_coral)
            }
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }


}