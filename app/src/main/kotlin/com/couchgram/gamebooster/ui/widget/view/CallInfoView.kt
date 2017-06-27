package com.couchgram.gamebooster.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.couchgram.gamebooster.R
import kotlinx.android.synthetic.main.call_info.view.*

/**
 * Created by chonamdoo on 2017. 5. 27..
 */

class CallInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(context, attrs,defStyle){
    var acceptCallListener: OnClickListener?= null
    var disconnectCallListener: OnClickListener?= null
    init {
        View.inflate(context, R.layout.call_info,this)
        accept_call.setOnClickListener {
            acceptCallListener?.onClick(it)
        }
        disconnect_call.setOnClickListener {
            disconnectCallListener?.onClick(it)
        }
    }
    fun setAcceptCallClickListener(listener: OnClickListener){
        this.acceptCallListener = listener
    }
    fun disconnectCallClickListener(listener: OnClickListener){
        disconnectCallListener = listener
    }
    fun setPhoneNumber(phoneNumber: String){
        txt_phoneNumber.text = phoneNumber
    }
    fun setName(name : String){
        txt_name.text = name
    }
    fun setAcceptCallDisabled(disabled : Boolean){
        if(disabled){
            accept_call.setBackgroundResource(R.drawable.bg_accept_call_disabled)
            accept_call.setImageResource(R.drawable.ic_icalling)
        }else{
            accept_call.setBackgroundResource(R.drawable.bg_accept_call)
            accept_call.setImageResource(R.drawable.ic_incommingcall)
        }
    }
}