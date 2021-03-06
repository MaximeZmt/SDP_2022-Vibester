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
     * change the view from one element to another with the given ids
     * @param a ID of the first element
     * @param b ID of the second element
     * @param itemView View contains both elements
     */
    fun changeAToB(a: Int, b: Int, itemView: View) {
        switchViewsVisibility(
            itemView.findViewById(a), itemView.findViewById(b)
        )
    }

    /**
     * switch the visibility between two view
     * @param invisible view to hide
     * @param visible view to show
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