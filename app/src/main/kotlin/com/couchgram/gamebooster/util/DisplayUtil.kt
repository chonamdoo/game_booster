package com.couchgram.gamebooster.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.data.preference.Pref

object DisplayUtil {

    private val BASE_GUIDE_WIDTH = 720f
    private val BASE_GUIDE_HEIGHT = 1280f

    @JvmStatic
    fun getDpi(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    @JvmStatic
    fun getPixelFromDp(context: Context, dp: Float): Int {
        return (getDpi(context) * dp).toInt()
    }

    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    @JvmStatic
    fun getWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    @JvmStatic
    fun getHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    /**
     * 현재 가로 해상도에 맞춰 리턴
     * @param px
     * *
     * @return
     */
    @JvmStatic
    fun fromW(context: Context, px: Float): Float {
        return if (px > 0) getWidth(context) * (px / BASE_GUIDE_WIDTH) else 0f
    }

    /**
     * 현재 세로 해상도에 맞춰 리턴
     * @param px
     * *
     * @return
     */
    @JvmStatic
    fun fromH(context: Context, px: Float): Float {
        return if (px > 0) getHeight(context) * (px / BASE_GUIDE_HEIGHT) else 0f
    }

    @JvmStatic
    fun setSizeFromW(context: Context, v: View, w: Float, h: Float) {
        setSize(v, fromW(context, w), fromW(context, h))
    }

    @JvmStatic
    fun setSizeFromH(context: Context, v: View, w: Float, h: Float) {
        setSize(v, fromH(context, w), fromH(context, h))
    }

    @JvmStatic
    fun setSize(v: View, w: Float, h: Float) {
        if (w > 0) v.layoutParams.width = w.toInt()
        if (h > 0) v.layoutParams.height = h.toInt()
    }

    @JvmStatic
    fun setMarginFromW(context: Context, v: View, l: Float, t: Float, r: Float, b: Float) {
        setMargin(v, fromW(context, l), fromW(context, t), fromW(context, r), fromW(context, b))
    }

    @JvmStatic
    fun setMarginFromH(context: Context, v: View, l: Float, t: Float, r: Float, b: Float) {
        setMargin(v, fromH(context, l), fromH(context, t), fromH(context, r), fromH(context, b))
    }

    @JvmStatic
    fun setMargin(v: View, l: Float, t: Float, r: Float, b: Float) {
        (v.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = l.toInt()
        (v.layoutParams as ViewGroup.MarginLayoutParams).topMargin = t.toInt()
        (v.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = r.toInt()
        (v.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = b.toInt()
    }
    @JvmStatic
    fun getStatusBarHeight(): Int {
        var result = 0
        try {
            val resourceId = AppContext.getInstance().resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = AppContext.getInstance().resources.getDimensionPixelSize(resourceId)
            }
        } catch (e: Exception) {
        } finally {
        }
        return result
    }

    fun dpTopx(dpValue: Float): Float {
        val scale = AppContext.getInstance().resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }


    fun pxTodp(pxValue: Float): Float {
        val scale = AppContext.getInstance().resources.displayMetrics.density
        return (pxValue / scale + 0.5f)
    }

    fun pxTosp(pxValue: Float): Float {
        val fontScale = AppContext.getInstance().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f)
    }

    fun spTopx(spValue: Float): Float {
        val fontScale = AppContext.getInstance().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f)
    }
}