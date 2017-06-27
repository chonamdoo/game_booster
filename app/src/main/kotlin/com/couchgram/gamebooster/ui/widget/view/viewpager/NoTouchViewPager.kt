package com.couchgram.gamebooster.ui.widget.view.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent



/**
 * Created by chonamdoo on 2017. 5. 2..
 */
class NoTouchViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
)
    : ViewPager(context, attrs) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }
}