package com.couchgram.gamebooster.call

import android.content.Context
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.CallSate
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.PhoneUtils
import com.threshold.rxbus2.RxBus
import java.util.*

/**
 * Created by chonamdoo on 2017. 5. 25..
 */

class PhoneCallStateReceiver : PhonecallReceiver(){
    override fun onIncomingCallStarted(ctx: Context, number: String, start: Date) {
        RxBus.getInstance().post(CallSate(Constants.ON_CALL,number))
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String, start: Date) {

    }

    override fun onIncomingCallAccept(ctx: Context, number: String, start: Date) {
        RxBus.getInstance().post(CallSate(Constants.ACCEPT_CALL,number))
    }

    override fun onIncomingCallEnded(ctx: Context, number: String, start: Date, end: Date) {
        RxBus.getInstance().post(CallSate(Constants.END_CALL,number))
    }

    override fun onOutgoingCallEnded(ctx: Context, number: String, start: Date, end: Date) {
    }

    override fun onMissedCall(ctx: Context, number: String, start: Date) {
        RxBus.getInstance().post(CallSate(Constants.END_CALL,number))
    }

}