package com.couchgram.gamebooster.service

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import java.lang.reflect.Method


/**
 * Created by chonamdoo on 2017. 5. 30..
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class MonitorNotificationService : NotificationListenerService(){


    override fun onBind(mIntent: Intent): IBinder? {
        LogUtils.v("DEBUG800","MonitorNotificationService onBind")
        return super.onBind(mIntent)
    }

    override fun onUnbind(mIntent: Intent): Boolean {
        return super.onUnbind(mIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onNotificationPosted(sbn: StatusBarNotification?) {

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {

    }
}