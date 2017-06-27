package com.couchgram.gamebooster.analytics

import android.os.Bundle

/**
 * Created by chonamdoo on 2017. 5. 11..
 */

open class Analytics {
    open fun logEvent(key: String, data: Bundle) {}
    open fun logEvent(category: String, action: String, label: String) {}
}