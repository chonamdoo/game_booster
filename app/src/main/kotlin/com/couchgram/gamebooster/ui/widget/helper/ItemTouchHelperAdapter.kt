package com.couchgram.gamebooster.ui.widget.helper

/**
 * Created by chonamdoo on 2017. 5. 8..
 */
interface ItemTouchHelperAdapter {
    fun onItemDrag(position: Int): Boolean
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
//    fun onItemSelectedChanged(viewHolder: RecyclerView.ViewHolder,@ItemTouch.STATUS status: Long)
}