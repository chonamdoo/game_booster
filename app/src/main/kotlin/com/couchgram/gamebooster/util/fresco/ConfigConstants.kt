package com.couchgram.gamebooster.util.fresco

import com.facebook.common.util.ByteConstants

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

object ConfigConstants{
    val CACHE_DIRECTORY_NAME = "couchgram"
    private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()
    val MAX_DISK_CACHE_SIZE = 80 * ByteConstants.MB.toLong()
    val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4
}