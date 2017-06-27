package com.couchgram.gamebooster.util.daemon

import android.content.Context
import com.couchgram.gamebooster.util.LogUtils
import java.io.File
import java.io.IOException

/**
 * Created by chonamdoo on 2017. 5. 1..
 */
object Daemon {
    private val TAG = Daemon::class.java.simpleName

    private val BIN_DIR_NAME = "bin"
    private val DAEMON_BIN_NAME = "daemon"

    val INTERVAL_ONE_MINUTE = 60
    val INTERVAL_ONE_HOUR = 3600

    /**
     * start daemon
     */
    private fun start(context: Context, daemonClazzName: Class<*>, interval: Int) {
        val cmd = context.getDir(BIN_DIR_NAME, Context.MODE_PRIVATE)
                .absolutePath + File.separator + DAEMON_BIN_NAME

        /* create the command string */
        val cmdBuilder = StringBuilder()
        cmdBuilder.append(cmd)
        cmdBuilder.append(" -p ")
        cmdBuilder.append(context.packageName)
        cmdBuilder.append(" -s ")
        cmdBuilder.append(daemonClazzName.name)
        cmdBuilder.append(" -t ")
        cmdBuilder.append(interval)

        try {
            Runtime.getRuntime().exec(cmdBuilder.toString()).waitFor()
        } catch (e: IOException) {
            LogUtils.e(TAG, "start daemon error: ${e.message}")
        } catch (e: InterruptedException) {
            LogUtils.e(TAG, "start daemon error: ${e.message}")
        }

    }

    /**
     * Run daemon process.

     * @param context            context
     * *
     * @param daemonServiceClazz the name of daemon service class
     * *
     * @param interval           the interval to check
     */
    fun run(context: Context, daemonServiceClazz: Class<*>,
            interval: Int) {
        Thread(Runnable {
            Command.install(context, BIN_DIR_NAME, DAEMON_BIN_NAME)
            start(context, daemonServiceClazz, interval)
        }).start()
    }
}