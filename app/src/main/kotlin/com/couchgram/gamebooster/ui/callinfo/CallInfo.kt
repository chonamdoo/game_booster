package com.couchgram.gamebooster.ui.callinfo

import android.content.Context
import android.graphics.PixelFormat
import android.net.Uri
import android.provider.ContactsContract
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.CallSate
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.ui.widget.view.CallInfoView
import com.couchgram.gamebooster.util.LogUtils


/**
 * Created by chonamdoo on 2017. 5. 27..
 */

class CallInfo(val context: Context,val event: CallSate) : View.OnTouchListener{
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0.toFloat()
    private var initialTouchY = 0.toFloat()
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val layoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_PHONE
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSPARENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.LEFT
            x = Pref.floatingX
            y = Pref.floatingY
        }
    }

    private val callInfoView : CallInfoView by lazy {
        CallInfoView(context)
    }

    init {
        callInfoView.setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = layoutParams.x
                initialY = layoutParams.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_MOVE -> {
                layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                Pref.floatingX = layoutParams.x
                Pref.floatingY = layoutParams.y
                windowManager.updateViewLayout(callInfoView, layoutParams)
            }
        }
        return false
    }

    fun showCallInfo(){
        try {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(event.phoneNumber))
            val cursor = context.contentResolver.query(uri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),null,null,null)
            var name : String = "모르는 번호"
            cursor?.use {
                if(it.moveToNext()) {
                    name = it.getString(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
                }
            }
            if(event.state == Constants.ACCEPT_CALL){
                callInfoView.setAcceptCallDisabled(true)
            }else{
                callInfoView.setAcceptCallDisabled(false)
            }
            callInfoView.setName(name)
            callInfoView.setPhoneNumber(event.phoneNumber)
            windowManager.addView(callInfoView,layoutParams)
        }catch (e : Exception){

        }
    }

    fun hideCallInfo(){
        try{
            windowManager.removeViewImmediate(callInfoView)
        }catch (e : Exception){

        }
    }
    fun setAcceptCallClickListener(listener: View.OnClickListener){
        callInfoView.setAcceptCallClickListener(listener)
    }
    fun setDisconnectCallClickListener(listener: View.OnClickListener){
        callInfoView.disconnectCallClickListener(listener)
    }
}