package com.couchgram.gamebooster.ui.widget.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.view.View




/**
 * Created by chonamdoo on 2017. 5. 18..
 */

class LinearLayouItemDecoration(val size: Int,val orientation: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,state: RecyclerView.State) {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, size, 0)
        } else {
            outRect.set(0, 0, 0, size)
        }
    }
}