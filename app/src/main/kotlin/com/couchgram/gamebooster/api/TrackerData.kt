package com.couchgram.gamebooster.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by chonamdoo on 2017. 5. 7..
 */
class TrackerData {
    @SerializedName("result")
    @Expose
    var result: String? = null
}