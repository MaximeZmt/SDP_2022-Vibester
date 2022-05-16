package ch.sdp.vibester.helper

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.user.OnItemClickListener
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

/** class contains helper functions for adapter of recycleView to reduce duplicates*/
class AdapterHelper {

    /**
     * @param position position of the item in the recycle view
     * @param listener listener of the item on click
     * verify the item still exits then tell the listener it has been click
     */
    fun onClickHelper(position: Int, listener: OnItemClickListener?) {
        //check the item is not deleted between time
        if (position != RecyclerView.NO_POSITION) {
            listener?.onItemClick(position)
        }
    }

    /**
     * @param buttonId ID of the button
     * @param imageId ID of the image
     * @param itemView View contains the button and image
     * change the view from button to image in itemView
     */
    fun changeBtnToImageHelper(buttonId: Int, imageId: Int, itemView: View) {
        itemView.findViewById<Button>(buttonId).visibility = View.INVISIBLE
        itemView.findViewById<ImageView>(imageId).visibility = View.VISIBLE
    }

    /**
     * @param parent
     * @param layoutId id of the layout
     * helper function creates the view used in function onCreateViewHolder
     */
    fun createViewForViewHolder(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

}