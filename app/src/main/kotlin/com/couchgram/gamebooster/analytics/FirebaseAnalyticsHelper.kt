package com.couchgram.gamebooster.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by chonamdoo on 2017. 5. 11..
 */
class FirebaseAnalyticsHelper(context: Context) : Analytics() {
    val intance = FirebaseAnalytics.getInstance(context)
    override fun logEvent(key: String, data: Bundle) {
        try {
            intance?.logEvent(key, data)
        } catch (e: Exception) {
        }
    }
    override fun logEvent(category: String, action: String, label: String){
        try{
            val arguments = Bundle().apply {
                putString(action, if (label.isNullOrEmpty()) action else label)
            }
            logEvent(category, arguments)
        }catch (e: Exception){

        }
    }
}