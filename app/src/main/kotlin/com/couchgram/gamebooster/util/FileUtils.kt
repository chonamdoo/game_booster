package com.couchgram.gamebooster.util

import java.util.*

/**
 * Created by chonamdoo on 2017. 5. 11..
 */
object FileUtils {
    fun getFormatMemSize(paramLong: Long): String {
        if (paramLong < 1024) return paramLong.toString() + " B"
        val z = (63 - java.lang.Long.numberOfLeadingZeros(paramLong)) / 10
        return String.format(Locale.US, "%d %sB", Math.round(paramLong.toDouble() / (1L shl z * 10)).toInt(), " KMGTPE"[z])
    }
}