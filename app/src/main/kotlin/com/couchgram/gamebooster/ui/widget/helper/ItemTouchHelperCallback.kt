package com.couchgram.gamebooster.ui.widget.helper

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.GridLayoutManager
import com.couchgram.gamebooster.util.LogUtils


/**
 * Created by chonamdoo on 2017. 5. 7..
 */
class ItemTouchHelperCallback(val itemTouchHelperAdapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        var swipeFlags = 0
        val dragFlags : Int
        if (recyclerView?.layoutManager is GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        } else {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        }
        return viewHolder?.let {
            if(itemTouchHelperAdapter.onItemDrag(it.adapterPosition)){
                ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
            }else{
                0
            }
        }?:ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if (viewHolder?.itemViewType != target?.itemViewType) {
            return false
        }
        itemTouchHelperAdapter.onItemMove(viewHolder?.adapterPosition?.let{it}?:-1,target?.adapterPosition?.let { it }?:-1)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.let {
                if(it is ItemTouchHelperViewHolder){
                    it.onItemSelected()
                }
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        viewHolder?.let {
            if(it is ItemTouchHelperViewHolder){
                it.onItemClear()
            }
        }
    }
}