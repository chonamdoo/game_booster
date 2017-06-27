package com.couchgram.gamebooster.util

import android.util.Log
import com.couchgram.gamebooster.BuildConfig
import com.couchgram.gamebooster.data.preference.Pref

/**
 * Created by chonamdoo on 2017. 5. 6..
 */
object LogUtils {
    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG || Pref.isDevelopMode) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG|| Pref.isDevelopMode) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG || Pref.isDevelopMode) {
            Log.i(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG || Pref.isDevelopMode) {
            Log.e(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG || Pref.isDevelopMode) {
            Log.w(tag, msg)
        }
    }

}