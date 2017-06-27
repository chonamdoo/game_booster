package com.couchgram.gamebooster.ui.widget.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper


/**
 * Created by chonamdoo on 2017. 5. 18..
 */

class GridLayoutItemDecoration : RecyclerView.ItemDecoration(){
    private var orientation = -1
    private var spanCount = -1
    private var spacing: Int = 0
    private var halfSpacing: Int = 0
    private val VERTICAL = OrientationHelper.VERTICAL

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        super.getItemOffsets(outRect, view, parent, state)

        if (orientation == -1) {
            orientation = getOrientation(parent)
        }

        if (spanCount == -1) {
            spanCount = getTotalSpan(parent)
        }

        val childCount = parent.layoutManager.itemCount
        val childIndex = parent.getChildAdapterPosition(view)

        val itemSpanSize = getItemSpanSize(parent, childIndex)
        val spanIndex = getItemSpanIndex(parent, childIndex)

        /* INVALID SPAN */
        if (spanCount < 1) return

        setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex)
    }

    fun setSpacings(outRect: Rect, parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int) {

        outRect.top = halfSpacing
        outRect.bottom = halfSpacing
        outRect.left = halfSpacing
        outRect.right = halfSpacing

        if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.top = spacing
        }

        if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.left = spacing
        }

        if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.right = spacing
        }

        if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.bottom = spacing
        }
    }

    fun getTotalSpan(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is StaggeredGridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    internal fun getItemSpanSize(parent: RecyclerView, childIndex: Int): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanSizeLookup.getSpanSize(childIndex)
        } else if (mgr is StaggeredGridLayoutManager) {
            return 1
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    internal fun getItemSpanIndex(parent: RecyclerView, childIndex: Int): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanSizeLookup.getSpanIndex(childIndex, spanCount)
        } else if (mgr is StaggeredGridLayoutManager) {
            return childIndex % spanCount
        } else if (mgr is LinearLayoutManager) {
            return 0
        }

        return -1
    }

    fun getOrientation(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        if (mgr is LinearLayoutManager) {
            return mgr.orientation
        } else if (mgr is GridLayoutManager) {
            return mgr.orientation
        } else if (mgr is StaggeredGridLayoutManager) {
            return mgr.orientation
        }

        return VERTICAL
    }

    internal fun isLeftEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {

        if (orientation == VERTICAL) {

            return spanIndex == 0

        } else {

            return childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)
        }
    }

    internal fun isRightEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {

        if (orientation == VERTICAL) {

            return spanIndex + itemSpanSize == spanCount

        } else {

            return isLastItemEdgeValid(childIndex >= childCount - spanCount, parent, childCount, childIndex, spanIndex)
        }
    }

    fun isTopEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {

        if (orientation == VERTICAL) {

            return childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)

        } else {

            return spanIndex == 0
        }
    }

    fun isBottomEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {

        if (orientation == VERTICAL) {

            return isLastItemEdgeValid(childIndex >= childCount - spanCount, parent, childCount, childIndex, spanIndex)

        } else {

            return spanIndex + itemSpanSize == spanCount
        }
    }

    fun isFirstItemEdgeValid(isOneOfFirstItems: Boolean, parent: RecyclerView, childIndex: Int): Boolean {

        var totalSpanArea = 0
        if (isOneOfFirstItems) {
            for (i in childIndex downTo 0) {
                totalSpanArea = totalSpanArea + getItemSpanSize(parent, i)
            }
        }

        return isOneOfFirstItems && totalSpanArea <= spanCount
    }

    fun isLastItemEdgeValid(isOneOfLastItems: Boolean, parent: RecyclerView, childCount: Int, childIndex: Int, spanIndex: Int): Boolean {

        var totalSpanRemaining = 0
        if (isOneOfLastItems) {
            for (i in childIndex..childCount - 1) {
                totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i)
            }
        }

        return isOneOfLastItems && totalSpanRemaining <= spanCount - spanIndex
    }
}