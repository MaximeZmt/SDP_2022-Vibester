package ch.sdp.vibester.model

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.helper.AdapterHelper
import ch.sdp.vibester.user.OnItemClickListener

class SongListAdapterForDelete constructor(
    private val songList: ArrayList<String>, private val listener: OnItemClickListener?
) : RecyclerView.Adapter<SongListAdapterForDelete.SongListForDeleteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListForDeleteViewHolder {
        return SongListForDeleteViewHolder(
            AdapterHelper().createViewForViewHolder(parent, R.layout.song_item_delete_layout)
        )
    }

    override fun onBindViewHolder(holder: SongListForDeleteViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    override fun getItemCount() = songList.size

    inner class SongListForDeleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bind(songName: String) {
            itemView.findViewById<TextView>(R.id.delete_song_name).text = songName
            itemView.findViewById<ImageView>(R.id.song_delete).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            AdapterHelper().onClickHelper(adapterPosition, listener)
        }

    }
}