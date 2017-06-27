package com.couchgram.gamebooster.util

import android.app.ActivityManager
import android.content.Context
import com.couchgram.gamebooster.AppContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException




/**
 * Created by chonamdoo on 2017. 5. 18..
 */

object RamUtils{
    val MEMORY_UNIT = 1048576L
    fun getUseMemPercent() : Int{
        val memoryInfo = ActivityManager.MemoryInfo()
        (AppContext.getInstance().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        val avaiMem= memoryInfo.availMem
        val totalProcMem = getTotalMemory()
        val totalMem = if(totalProcMem > 0 ) totalProcMem else memoryInfo.totalMem
        return (100L * (totalMem - avaiMem) / totalMem).toInt()
    }
    fun getUseMemSize() : Long{
        val memoryInfo = ActivityManager.MemoryInfo()
        (AppContext.getInstance().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem - memoryInfo.availMem
    }
    fun getAvailMemSize() : Long{
        val memoryInfo = ActivityManager.MemoryInfo()
        (AppContext.getInstance().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }
    fun getUseMemPercent(cleanSize : Long) : Int{
        val memoryInfo = ActivityManager.MemoryInfo()
        (AppContext.getInstance().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        val avaiMem= memoryInfo.availMem
        val totalProcMem = getTotalMemory()
        val totalMem = if(totalProcMem > 0 ) totalProcMem else memoryInfo.totalMem
        return (100L * (totalMem - (avaiMem+cleanSize)) / totalMem).toInt()
    }

    fun getTotalMemory(): Long {
        val str1 = "/proc/meminfo"
        var initial_memory: Long = 0
        try {
            val localBufferedReader = BufferedReader(FileReader(str1), 8192)
            localBufferedReader.use {
                localBufferedReader->
                val line = localBufferedReader.readLine()
                initial_memory = line.substring(line.indexOf(":") + 1, line.indexOf("kB")).trim().toLong() * 1024L
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return initial_memory
    }
}