package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView
/**
 * Created by chonamdoo on 2017. 5. 6..
 */
class NoneScrollableGridView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
)  : GridView(context,attrs,defStyle){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightSpec)
        layoutParams.height = measuredHeight
    }
}