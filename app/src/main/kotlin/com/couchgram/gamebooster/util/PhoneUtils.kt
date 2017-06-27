package com.couchgram.gamebooster.util

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.session.MediaSessionManager
import android.os.Build
import android.telephony.TelephonyManager
import android.view.KeyEvent
import com.android.internal.telephony.ITelephony
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.service.MonitorNotificationService
import java.io.IOException
import java.lang.reflect.Method
import android.content.Context.AUDIO_SERVICE



/**
 * Created by chonamdoo on 2017. 5. 30..
 */

object PhoneUtils {
    private val MANUFACTURER_HTC = "HTC"
    fun acceptCall() {
        if (android.os.Build.VERSION.SDK_INT < 21) {
            if (android.os.Build.VERSION.SDK_INT < 23) {
                acceptCallPhoneAidl(AppContext.getInstance())
            } else {
                acceptCall_4_1(AppContext.getInstance())
            }
        } else {
            acceptCallHeadsetHookLollipop(AppContext.getInstance())
        }
    }

    fun disconnectCall() {
        val telephonyManager = AppContext.getInstance().getSystemService(Context.TELEPHONY_SERVICE)
        var declaredMethod: Method
        try {
            declaredMethod = telephonyManager.javaClass.getDeclaredMethod("getITelephony", *arrayOfNulls<Class<*>>(0))
            declaredMethod.isAccessible = true
            (declaredMethod.invoke(telephonyManager, *arrayOfNulls<Any>(0)) as ITelephony).endCall()
        } catch (e1: Exception) {
            LogUtils.v(TAG, "disconnectCall error2 : " + e1.message)
            try {
                declaredMethod = telephonyManager.javaClass.getDeclaredMethod("getITelephonyMSim", *arrayOfNulls<Class<*>>(0))
                declaredMethod.isAccessible = true
                val invoke = declaredMethod.invoke(telephonyManager, *arrayOfNulls<Any>(0))
                val declaredMethod2 = invoke.javaClass.getDeclaredMethod("endCall", *arrayOf<Class<*>>(Integer.TYPE))
                declaredMethod2.isAccessible = true
                val invoke2 = declaredMethod2.invoke(invoke, *arrayOf<Any>(Integer.valueOf(0))) as Boolean
                if (!invoke2 ) {
                    declaredMethod2.invoke(invoke, *arrayOf<Any>(Integer.valueOf(1)))
                }
            } catch (e2: Exception) {

            }
        }
    }

    private fun acceptCallPhoneAidl(context: Context) {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val m = Class.forName(tm.javaClass.name).getDeclaredMethod("getITelephony", *arrayOfNulls<Class<*>>(0))
            m.isAccessible = true
            val telephonyService = m.invoke(tm, *arrayOfNulls<Any>(0)) as ITelephony
            LogUtils.v(TAG, "Phone answer incoming call now!")
            telephonyService.answerRingingCall()
            // telephonyService.silenceRinger();


        } catch (e: Exception) {
            acceptCall_4_1(context)
        }

    }
    private fun acceptCall_4_1(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // for HTC devices we need to broadcast a connected headset
        val broadcastConnected = MANUFACTURER_HTC.equals(Build.MANUFACTURER, ignoreCase = true) && !audioManager.isWiredHeadsetOn
        if (broadcastConnected) {
            broadcastHeadsetConnected(context, false)
        }
        try {
            try {
                Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK))
            } catch (e: IOException) {
                val enforcedPerm = "android.permission.CALL_PRIVILEGED"
                val btnDown = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_HEADSETHOOK))
                val btnUp = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_HEADSETHOOK))
                context.sendOrderedBroadcast(btnDown, enforcedPerm)
                context.sendOrderedBroadcast(btnUp, enforcedPerm)
            }

        } finally {
            if (broadcastConnected) {
                broadcastHeadsetConnected(context, false)
            }
        }
    }

    private fun broadcastHeadsetConnected(context: Context, connected: Boolean) {
        try {
            context.sendOrderedBroadcast(Intent(Intent.ACTION_HEADSET_PLUG).apply {
                addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
                putExtra("state", if (connected) 1 else 0)
                putExtra("name", "mysms")
            }, null)
        } catch (e: Exception) {
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun acceptCallHeadsetHookLollipop(context: Context) {
        val mediasessionmanager = context.applicationContext.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        try {
            val mediaControllerList = mediasessionmanager.getActiveSessions(ComponentName(context.applicationContext,MonitorNotificationService::class.java.canonicalName))
            for (m in mediaControllerList) {
                if ("com.android.server.telecom" == m.packageName) {
                    m.dispatchMediaButtonEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK))
                    break
                }
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "e : ${e.message}")

        }
    }

    fun silenceRinger(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            context.startService(Intent(context,MonitorNotificationService::class.java).apply {
                putExtra("silent",true)
            })
        }else{
            (context.getSystemService(AUDIO_SERVICE) as AudioManager).ringerMode = AudioManager.RINGER_MODE_SILENT
        }
        /*val start = intent.getBooleanExtra("start", false)
        if (start) {
            Log.d("TAG", "START")

            //Check if at least Lollipop, otherwise use old method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                requestInterruptionFilter(INTERRUPTION_FILTER_NONE)
            else {
                (context.getSystemService(AUDIO_SERVICE) as AudioManager).ringerMode = AudioManager.RINGER_MODE_SILENT
            }
        } else {
            Log.d("TAG", "STOP")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                requestInterruptionFilter(INTERRUPTION_FILTER_ALL)
            else {
                getBaseContext().getSystemService(AUDIO_SERVICE).ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        }*/
    }
}