package com.couchgram.gamebooster.analytics

import android.os.Bundle
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.TAG
import java.util.ArrayList

/**
 * Created by chonamdoo on 2017. 5. 11..
 */
class AnalyticsHelper {
    val mAnalyticsList = ArrayList<Analytics>()
    init {
        mAnalyticsList.add(GoogleAnalyticsHelper(AppContext.getInstance()))
        mAnalyticsList.add(FirebaseAnalyticsHelper(AppContext.getInstance()))
    }

    fun logEvent(key: String, data: Bundle) {
        for (analytics in mAnalyticsList) {
            analytics.logEvent(key, data)
        }
    }

    fun logEvent(category: String, action: String, label: String) {
        LogUtils.v(AnalyticsHelper.TAG, "GOOGLE : $category / $action / $label")
        for (analytics in mAnalyticsList) {
            analytics.logEvent(category, action, label)
        }
    }
    companion object{
        fun getInstance() = AnalyticsHelper()
    }
}