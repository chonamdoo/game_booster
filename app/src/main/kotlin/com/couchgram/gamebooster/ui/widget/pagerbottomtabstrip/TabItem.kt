package com.couchgram.gamebooster.ui.widget.pagerbottomtabstrip

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.util.DisplayUtil
import kotlinx.android.synthetic.main.tab_item.view.*
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem

/**
 * Created by chonamdoo on 2017. 5. 2..
 */

class TabItem(context : Context) : BaseTabItem(context){
    private var mDefaultDrawable: Int = 0
    private var mCheckedDrawable: Int = 0
    private var mDefaultTabColor: Int = Color.WHITE
    private var mCheckedTabColor: Int = Color.WHITE
    private var mDefaultTextColor = Color.BLACK
    private var mCheckedTextColor = Color.BLACK
    init {
        LayoutInflater.from(context).inflate(R.layout.tab_item, this, true)
    }
    override fun setChecked(checked: Boolean) {
        tab_icon.setImageResource(when(checked){
            true -> mCheckedDrawable
            else -> mDefaultDrawable
        })
        tab_title.setTextColor(when(checked){
            true -> mCheckedTextColor
            else -> mDefaultTextColor
        })
        /*tab_title.textSize = when(checked){
            true -> 14F
            else -> 13F
        }*/
        setBackgroundColor(when(checked){
            true -> mCheckedTabColor
            else -> mDefaultTabColor
        })
    }

    override fun getTitle(): String {
        return tab_title.text.toString()
    }

    override fun setMessageNumber(number: Int) {
    }

    override fun setHasMessage(hasMessage: Boolean) {
    }

    fun initialize(@DrawableRes drawableRes: Int, @DrawableRes checkedDrawableRes: Int) {
        mDefaultDrawable = drawableRes
        mCheckedDrawable = checkedDrawableRes
    }
    fun setTitle(title : String){
        tab_title.text = title
    }

    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }
    fun setTabDefaultColor(@ColorInt color: Int){
        mDefaultTabColor = color
    }
    fun setTabCheckColor(@ColorInt color: Int){
        mCheckedTabColor = color
    }
}