package ch.sdp.vibester.helper

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.user.OnItemClickListener
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class AdapterHelper {

    fun onClickHelper(listener: OnItemClickListener, position: Int) {
        //check the item is not deleted between time
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
        }
    }

    fun changeBtnToImageHelper(buttonId: Int, imageId: Int, itemView: View) {
        itemView.findViewById<Button>(buttonId).visibility = View.INVISIBLE
        itemView.findViewById<ImageView>(imageId).visibility = View.VISIBLE
    }

    fun createViewForViewHolder(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

}