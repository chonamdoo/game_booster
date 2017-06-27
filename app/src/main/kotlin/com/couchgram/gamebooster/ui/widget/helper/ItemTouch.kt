package com.couchgram.gamebooster.ui.widget.helper

import android.support.annotation.IntDef

/**
 * Created by chonamdoo on 2017. 5. 8..
 */
object ItemTouch {
    const val ITEM_SELECTED = 0L
    const val ITEM_SELECTED_CANCEL = 1L

    @IntDef(ITEM_SELECTED, ITEM_SELECTED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class STATUS
}