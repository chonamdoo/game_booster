package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.couchgram.gamebooster.R
import kotlinx.android.synthetic.main.rocket_progress.view.*

/**
 * Created by chonamdoo on 2017. 5. 29..
 */

class RocketProgressbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(context, attrs,defStyle){
    init {
        View.inflate(context, R.layout.rocket_progress,this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation_view.cancelAnimation()
        animation_view.clearAnimation()
    }
}