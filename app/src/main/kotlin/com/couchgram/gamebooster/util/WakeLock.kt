package com.couchgram.gamebooster.util

import android.os.PowerManager
import android.app.KeyguardManager
import android.content.Context
import com.couchgram.gamebooster.AppContext


/**
 * Created by chonamdoo on 2017. 6. 9..
 */
object WakeLock {
    private var sCpuWakeLock: PowerManager.WakeLock? = null
    private val mKeyguardLock: KeyguardManager.KeyguardLock? = null
    private val isScreenLock: Boolean = false
    private val powerManager : PowerManager by lazy {
        AppContext.getInstance().getSystemService(Context.POWER_SERVICE) as PowerManager
    }
    fun acquireCpuWakeLock() {
        LogUtils.e("PushWakeLock", "Acquiring cpu wake lock")

        sCpuWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "wakelock").apply {
            acquire()
        }
    }

    fun releaseCpuLock() {
        sCpuWakeLock?.let {
            it.release()
        }
    }
}