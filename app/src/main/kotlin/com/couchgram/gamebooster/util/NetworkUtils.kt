package com.couchgram.gamebooster.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import com.couchgram.gamebooster.AppContext

/**
 * Created by chonamdoo on 2017. 5. 8..
 */
object NetworkUtils {
    fun isWifiConnected(context: Context): Boolean {
        return isNetworkConnected(context, ConnectivityManager.TYPE_WIFI)
    }

    fun isMobileConnected(context: Context): Boolean {
        return isNetworkConnected(context, ConnectivityManager.TYPE_MOBILE)
    }

    private fun isNetworkConnected(context: Context, type: Int): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val networkInfo = connectivityManager.getNetworkInfo(type)
            return networkInfo != null && networkInfo.isConnected
        } else {
            val networks = connectivityManager.allNetworks
            var networkInfo: NetworkInfo?
            for (network in networks) {
                networkInfo = connectivityManager.getNetworkInfo(network)
                if (networkInfo != null && networkInfo.isConnected && networkInfo.type == type)
                    return true
            }
            return false
        }
    }

    fun isAirplaneModeOn(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(AppContext.getInstance().contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0
        } else {
            return Settings.Global.getInt(AppContext.getInstance().contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        }
    }

    fun isNetworkConnected(): Boolean {
        try {
            val networkInfo = (AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED && !isAirplaneModeOn())
                return true
            return false
        } catch (e: Exception) {
            return false
        }

    }
}