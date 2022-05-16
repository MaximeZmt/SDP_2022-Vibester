package ch.sdp.vibester.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.AdapterHelper


/**
 * SongListAdapter to set correct/wrong guessed songs in the game.
 */
class SongListAdapter constructor( private val incorrectSongList: ArrayList<String>,
                                   private val correctSongList: ArrayList<String> ):
    RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {
    private val songList: ArrayList<String> = arrayListOf()

    init{
        songList.addAll(incorrectSongList)
        songList.addAll(correctSongList)
    }

    /**
     * Create a RecycleView layout with the Song view as an item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = AdapterHelper().createViewForViewHolder(parent, R.layout.song_item_layout)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    /**
     * Get amount of songs displayed
     */
    override fun getItemCount(): Int {
        return songList.size
    }


    /**
     * Customer ViewHolder class for SongList. Each item contains songname and a button.
     */
    inner class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(songName: String) {
            itemView.findViewById<TextView>(R.id.song_name).text = songName
            val downloadSongBtn = itemView.findViewById<Button>(R.id.song_download)

            downloadSongBtn.setOnClickListener {
                AdapterHelper().changeBtnToImageHelper(R.id.song_download, R.id.song_download_done, itemView)
            }

            // Make background red if song is guessed incorrectly
            if(songName in incorrectSongList) {
                itemView.setBackgroundResource(R.color.light_coral);
            }
        }

    }


}