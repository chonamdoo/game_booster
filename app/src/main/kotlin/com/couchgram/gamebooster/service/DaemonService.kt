package com.couchgram.gamebooster.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.couchgram.gamebooster.util.daemon.Daemon

/**
 * Created by chonamdoo on 2017. 4. 25..
 */

class DaemonService : Service(){

    override fun onCreate() {
        Daemon.run(this,DaemonService::class.java,Daemon.INTERVAL_ONE_MINUTE)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}