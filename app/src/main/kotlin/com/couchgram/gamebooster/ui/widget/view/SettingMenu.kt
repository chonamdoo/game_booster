package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.ui.widget.listener.SettingMenuClickListener
import kotlinx.android.synthetic.main.setting_menu.view.*

/**
 * Created by chonamdoo on 2017. 5. 30..
 */

class SettingMenu @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
): FrameLayout(context, attrs,defStyle){
    var listener : SettingMenuClickListener?= null
    init {

        LayoutInflater.from(context).inflate(R.layout.setting_menu, this, true)
        setting_layout.setOnClickListener {
            it.run {
                val switch = findViewById(R.id.setting_onoff) as SwitchCompat
                listener?.menuCheckChange(this@SettingMenu,!switch.isChecked)
            }
        }
        setting_onoff.setOnClickListener {
            listener?.menuCheckChange(this@SettingMenu,setting_onoff.isChecked)
        }
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,R.styleable.setting_menu, 0, 0)
            setting_title.run {
                setTextSize(TypedValue.COMPLEX_UNIT_PX,typedArray.getDimensionPixelSize(R.styleable.setting_menu_title_text_size,15).toFloat())
                text = typedArray.getString(R.styleable.setting_menu_title_text)
                visibility = if(text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            setting_desc.run {
                setTextSize(TypedValue.COMPLEX_UNIT_PX,typedArray.getDimensionPixelSize(R.styleable.setting_menu_desc_text_size,13).toFloat())
                text = typedArray.getString(R.styleable.setting_menu_desc_text)
                visibility = if(text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            typedArray.recycle()
        }
    }
    fun onCheckChangeListener(listener : SettingMenuClickListener){
        this.listener = listener
    }
    fun setChecked(checked : Boolean){
        setting_onoff.isChecked = checked
    }
}