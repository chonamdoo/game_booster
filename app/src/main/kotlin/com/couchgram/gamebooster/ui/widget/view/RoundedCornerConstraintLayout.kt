package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.View
import android.graphics.RectF
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth






/**
 * Created by chonamdoo on 2017. 5. 17..
 */
class RoundedCornerConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
)  : ConstraintLayout(context,attrs,defStyle){
    private val CORNER_RADIUS = 10f
    private var cornerRadius: Float = 0.toFloat()

    init {
        val metrics = context.resources.displayMetrics
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.save()
        val path = Path()
        val rect = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
        val arrayRadius = floatArrayOf(cornerRadius,cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius,cornerRadius, cornerRadius)


        path.addRoundRect(rect, arrayRadius, Path.Direction.CW)
        canvas.clipPath(path, Region.Op.REPLACE)
        canvas.clipPath(path)

        super.dispatchDraw(canvas)
        canvas.restoreToCount(count)
    }

}