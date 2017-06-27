package com.couchgram.gamebooster.analytics

import android.content.Context
import android.os.Bundle
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker

/**
 * Created by chonamdoo on 2017. 5. 11..
 */
class GoogleAnalyticsHelper(context: Context) : Analytics(){
    val instance = GoogleAnalytics.getInstance(context)
    var tracker : Tracker?= null
    init {
        instance?.let {
            tracker = it.newTracker("UA-98930633-1")?.apply {
                enableAdvertisingIdCollection(true)
                enableAutoActivityTracking(true)
                enableExceptionReporting(true)
            }
        }
    }

    override fun logEvent(key: String, data: Bundle) {

    }

    override fun logEvent(category: String, action: String, label: String) {
        try{
            tracker?.send(HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build())
        }catch (e : Exception){

        }
    }


}