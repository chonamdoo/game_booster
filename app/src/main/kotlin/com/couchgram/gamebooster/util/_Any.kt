package com.couchgram.gamebooster.util

import android.util.Log
import com.couchgram.gamebooster.BuildConfig
import com.couchgram.gamebooster.data.preference.Pref

/**
 * Created by chonamdoo on 2017. 5. 7..
 */


val Any.TAG: String get() = javaClass.simpleName


fun Any.v(message: String) {
    if (BuildConfig.DEBUG || Pref.isDevelopMode) {
        Log.v(this.javaClass.name, message)
    }
}

fun Any.d(message: String) {
    if (BuildConfig.DEBUG || Pref.isDevelopMode) {
        Log.d(this.javaClass.name, message)
    }
}

fun Any.i(message: String) {
    if (BuildConfig.DEBUG || Pref.isDevelopMode) {
        Log.i(this.javaClass.name, message)
    }
}

fun Any.w(message: String) {
    if (BuildConfig.DEBUG || Pref.isDevelopMode) {
        Log.w(this.javaClass.name, message)
    }
}

public fun Any.e(message: String) {
    if (BuildConfig.DEBUG || Pref.isDevelopMode) {
        Log.e(this.javaClass.name, message)
    }
}