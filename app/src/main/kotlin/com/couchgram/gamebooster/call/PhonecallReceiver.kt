package com.couchgram.gamebooster.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*

/**
 * Created by chonamdoo on 2017. 5. 25..
 */

abstract class PhonecallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (!TextUtils.isEmpty(action) && intent.extras != null) {
            if (action == ACTION_NEW_OUTGOING_CALL) {
                savedNumber = intent.extras.getString("android.intent.extra.PHONE_NUMBER")
            } else {
                val stateStr = intent.extras.getString(TelephonyManager.EXTRA_STATE)
                val number = intent.extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                var state = 0
                if (!TextUtils.isEmpty(stateStr)) {
                    if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                        state = TelephonyManager.CALL_STATE_IDLE
                    } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                        state = TelephonyManager.CALL_STATE_OFFHOOK
                    } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                        state = TelephonyManager.CALL_STATE_RINGING
                    }
                    onCallStateChanged(context, state, number)
                }
            }
        }
    }

    abstract fun onIncomingCallStarted(ctx: Context, number: String, start: Date)

    abstract fun onOutgoingCallStarted(ctx: Context, number: String, start: Date)

    abstract fun onIncomingCallAccept(ctx: Context, number: String, start: Date)

    abstract fun onIncomingCallEnded(ctx: Context, number: String, start: Date, end: Date)

    abstract fun onOutgoingCallEnded(ctx: Context, number: String, start: Date, end: Date)

    abstract fun onMissedCall(ctx: Context, number: String, start: Date)

    fun onCallStateChanged(context: Context, state: Int, number: String) {
        if (lastState == state) {
            return
        }
        if (lastState == TelephonyManager.CALL_STATE_OFFHOOK && state == TelephonyManager.CALL_STATE_RINGING) {
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                onIncomingCallStarted(context, number, callStartTime!!)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                isIncoming = false
                callStartTime = Date()
                onOutgoingCallStarted(context, savedNumber!!, callStartTime!!)
            } else {
                callStartTime = Date()
                onIncomingCallAccept(context, savedNumber!!, Date())
            }
            TelephonyManager.CALL_STATE_IDLE -> if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                onMissedCall(context, savedNumber!!, callStartTime!!)
            } else if (isIncoming) {
                onIncomingCallEnded(context, savedNumber!!, callStartTime!!, Date())
            } else {
                onOutgoingCallEnded(context, savedNumber!!, callStartTime!!, Date())
            }
        }
        lastState = state
    }

    companion object {
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming: Boolean = false
        private var savedNumber: String? = null
        private val ACTION_NEW_OUTGOING_CALL = Intent.ACTION_NEW_OUTGOING_CALL
    }
}