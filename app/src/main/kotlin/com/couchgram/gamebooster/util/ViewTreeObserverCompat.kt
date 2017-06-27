package com.couchgram.gamebooster.util

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by chonamdoo on 2017. 5. 29..
 */
object ViewTreeObserverCompat {
    fun addOnGlobalLayoutListener(
            view: View,
            listener: ViewTreeObserver.OnGlobalLayoutListener) {

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun removeOnGlobalLayoutListener(
            view: View,
            listener: ViewTreeObserver.OnGlobalLayoutListener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        } else {
            view.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        }
    }
}