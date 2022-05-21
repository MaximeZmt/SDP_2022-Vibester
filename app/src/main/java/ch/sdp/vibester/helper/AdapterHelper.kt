package ch.sdp.vibester.helper

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.user.OnItemClickListener
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

/** class contains helper functions for adapter of recycleView to reduce duplicates*/
class AdapterHelper {

    /**
     * verify the item still exits then tell the listener it has been click
     * @param position position of the item in the recycle view
     * @param listener listener of the item on click
     */
    fun onClickHelper(position: Int, listener: OnItemClickListener?) {
        //check the item is not deleted between time
        if (position != RecyclerView.NO_POSITION) {
            listener?.onItemClick(position)
        }
    }

    /**
     * change the view from button to image in itemView
     * @param buttonId ID of the button
     * @param imageId ID of the image
     * @param itemView View contains the button and image
     */
    fun changeBtnToImage(buttonId: Int, imageId: Int, itemView: View) {
        switchViewsVisibility(itemView.findViewById<Button>(buttonId), itemView.findViewById<ImageView>(imageId))
    }

    /**
     * switch the visibility between two view
     */
    fun switchViewsVisibility(invisible: View, visible: View) {
        invisible.visibility = INVISIBLE
        visible.visibility = VISIBLE
    }

    /**
     * helper function creates the view used in function onCreateViewHolder
     * @param parent
     * @param layoutId id of the layout
     */
    fun createViewForViewHolder(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

}