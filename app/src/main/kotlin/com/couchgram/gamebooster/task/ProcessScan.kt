package com.couchgram.gamebooster.task

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.RunningAppProcessInfo
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.data.source.boostapp.BoostAppRepository
import com.couchgram.gamebooster.util.FileUtils
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import com.couchgram.gamebooster.util.v
import io.reactivex.Single
import java.util.*

/**
 * Created by chonamdoo on 2017. 4. 28..
 */

class ProcessScan(val context : Context){
    private val whiteList =  setOf(context.packageName,Constants.COUCHGRAM,"com.samsung.sec.android.inputmethod.axt9",
            "com.android.phone","com.sec.android.app.dialertop","android","com.sec.mms",
            "com.android.alarmclock","com.sec.android.app.clockpackage","com.android.mms",
            "com.android.inputmethod.latin","com.android.providers.telephony",
            "com.spritemobile.backup.semc","com.spritemobile.backup.semc2","com.spritemobile.backup.semc",
            "com.android.incallui.InCallActivity",
            "com.htc.rosiewidgets.showme","com.htc.showme","system",Utils.getDefaultDialerApplication(AppContext.getInstance())?.let { it }?:"com.android.dialer")
    private val googleApps = setOf("com.google.android.googlequicksearchbox","com.google.android.music","com.ustwo.lwp")
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val packageManager = context.packageManager
    private val runningAppProcessInfoList = mutableListOf<RunningAppProcessInfo>()
    private val runningAppMap = hashMapOf<String,RunningAppProcessInfo>()
    fun runningTaskList() : MutableList<RunningAppProcessInfo>{
        try {
            runningAppProcessInfoList.clear()
            runningAppMap.clear()
            packageManager?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val runningServiceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE)
                    if (runningServiceInfoList != null) {
                        for (info in runningServiceInfoList) {
                            try {
                                val applicationInfo = packageManager.getApplicationInfo(info.service.packageName,0)
                                if ((!googleApps.contains(info.service.packageName) and ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM))
                                        || whiteList.contains(info.service.packageName)
                                        || runningAppMap.containsKey(info.service.packageName)) {
                                    continue
                                }
                                runningAppMap.put(info.service.packageName,RunningAppProcessInfo(info.pid,info.service.packageName,getMemInfo(info.pid,info.process)))
                            } catch (e: Exception) {
                                continue
                            }
                        }
                        runningAppProcessInfoList.addAll(runningAppMap.values)
                    }
                } else {
                    val appProcessList = activityManager.runningAppProcesses
                    if (appProcessList != null) {
                        for (info in appProcessList) {
                            try {
                                val applicationInfo = it.getApplicationInfo(info.processName, 0)
                                if ((!googleApps.contains(applicationInfo.packageName) and ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM))
                                            || whiteList.contains(applicationInfo.packageName)
                                            || runningAppMap.containsKey(applicationInfo.packageName)) {
                                    continue
                                }
                                runningAppMap.put(info.processName,RunningAppProcessInfo(info.pid,info.processName,getMemInfo(info.pid,info.processName)))
                            } catch (e: Exception) {
                                continue
                            }
                        }
                        runningAppProcessInfoList.addAll(runningAppMap.values)
                    }
                }
            }
        } catch (e: Exception) {

        }
        return runningAppProcessInfoList
    }
    fun cleanTask(runningAppProcessInfoList : MutableList<RunningAppProcessInfo>) : Long{
        var taskCleanSize : Long = 0
        if(runningAppProcessInfoList.isNotEmpty()){
            runningAppProcessInfoList.forEach {
                taskCleanSize += it.memSize
                killBackgroundProcesses(it.pid,it.packageName)
            }
        }
        return taskCleanSize
    }

    private fun killBackgroundProcesses(pid: Int, processName: String) {
        try {
            val packageName = if (!processName.contains(":")) { processName } else { processName.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] }
            restartPackage(packageName)
            killProcess(pid)
            activityManager.killBackgroundProcesses(packageName)
            val forceStopPackage = activityManager.javaClass.getDeclaredMethod("forceStopPackage", String::class.java)
            forceStopPackage.isAccessible = true
            forceStopPackage.invoke(activityManager, packageName)
        } catch (e: Exception) {
        }

    }

    private fun killProcess(pid: Int) {
        try {
            if(pid != -1) {
                Process.killProcess(pid)
            }
        } catch (e: Exception) {
        }
    }

    private fun getMemInfo(pid: Int , processName: String ): Long {
        val memoryInfo = activityManager.getProcessMemoryInfo(intArrayOf(pid))
        if (memoryInfo.isNotEmpty()) {
            val myMemoryInfo = memoryInfo[0]
            return if(processName.contains(":")) ((myMemoryInfo.totalPss * 1024L) + (myMemoryInfo.totalPss * 1024L) * 0.2F).toLong() else myMemoryInfo.totalPss * 1024L
        }
        return 0
    }

    private fun restartPackage(packageName: String) {
        try {
            activityManager.restartPackage(packageName)
        } catch (e: Exception) {

        }
    }
}