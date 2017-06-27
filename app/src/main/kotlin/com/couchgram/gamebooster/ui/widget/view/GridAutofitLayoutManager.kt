package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue



/**
 * Created by chonamdoo on 2017. 4. 29..
 */

class GridAutofitLayoutManager : GridLayoutManager {
    private var mColumnWidth: Int = 0
    private var mColumnWidthChanged = true

    constructor(context: Context, columnWidth: Int) : super(context, 1) {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    constructor(context: Context, columnWidth: Int, orientation: Int, reverseLayout: Boolean) : super(context, 1, orientation, reverseLayout) {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }


    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int {
        if(columnWidth <= 0){
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f,context.resources.displayMetrics).toInt()
        }else{
            return columnWidth
        }
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth
            mColumnWidthChanged = true
        }
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        if (mColumnWidthChanged && mColumnWidth > 0) {
            val totalSpace: Int
            if (orientation == LinearLayoutManager.VERTICAL) {
                totalSpace = width - paddingRight - paddingLeft
            } else {
                totalSpace = height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / mColumnWidth)
            setSpanCount(spanCount)
            mColumnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}