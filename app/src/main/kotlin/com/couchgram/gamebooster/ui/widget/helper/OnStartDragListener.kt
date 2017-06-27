package com.couchgram.gamebooster.ui.widget.helper

import android.support.v7.widget.RecyclerView

/**
 * Created by chonamdoo on 2017. 5. 8..
 */
interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    fun onEndDrag()
}