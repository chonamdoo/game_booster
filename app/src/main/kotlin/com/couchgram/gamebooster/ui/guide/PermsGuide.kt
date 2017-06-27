package com.couchgram.gamebooster.ui.guide

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.PERMS_GUIDE
import kotlinx.android.synthetic.main.accessibility_guide.view.*

/**
 * Created by chonamdoo on 2017. 5. 31..
 */

class PermsGuide(context : Context,val guideType : PERMS_GUIDE) {
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private val view : View by lazy{
        View.inflate(context,R.layout.accessibility_guide,null)
    }
    private val layoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_TOAST
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSPARENT
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.CENTER
            x = 0
            y = 0
        }
    }

    init {
        view.findViewById(R.id.btn_close).setOnClickListener {
            hide()
        }
    }

    fun show(){
        try {
            var jsonName = "json/setting_permissions.json"
            if(guideType == PERMS_GUIDE.NOTIFICATION_GUIDE){
                jsonName = "json/notification.json"
            }
            hide()
            view.animation_view.setAnimation(jsonName)
            view.animation_view.loop(true)
            view.animation_view.playAnimation()
            windowManager.addView(view,layoutParams)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    fun hide(){
        try{
            view.parent?.let {
                windowManager.removeViewImmediate(view)
            }
        }catch (e : Exception){

        }
    }
    fun dismiss(){
        try {
            view.animation_view.cancelAnimation()
            view.animation_view.clearAnimation()
            view.parent?.let {
                windowManager.removeViewImmediate(view)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}