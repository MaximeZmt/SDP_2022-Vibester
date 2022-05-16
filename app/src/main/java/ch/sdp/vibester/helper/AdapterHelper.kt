package ch.sdp.vibester.helper

import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.user.OnItemClickListener

class AdapterHelper {

    fun onClickHelper(listener: OnItemClickListener, position: Int) {
        //check the item is not deleted between time
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
        }
    }

}