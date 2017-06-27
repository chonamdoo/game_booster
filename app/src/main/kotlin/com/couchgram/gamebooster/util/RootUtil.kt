package com.couchgram.gamebooster.util

import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


/**
 * Created by chonamdoo on 2017. 6. 3..
 */

object RootUtil{
    private fun checkRootMethod1(): Boolean {
        val s = Build.TAGS
        return s != null && s.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        return arrayOf("/system/app/Superuser.apk","/sbin/su","/system/bin/su","/system/xbin/su",
                "/data/local/xbin/su","/data/local/bin/su","/system/sd/xbin/su","/system/bin/failsafe/su","/data/local/su")
                .any {
                    File(it).exists()
                }
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            return process?.let {
              val inputReader = BufferedReader(InputStreamReader(it.inputStream))
                inputReader.use {
                    it.readLine()?.let {
                        true
                    }?:false
                }
            }?:false
        } catch (t: Throwable) {
            return false
        } finally {
            process?.destroy()
        }
    }

    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }
}