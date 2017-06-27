package com.couchgram.gamebooster.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by chonamdoo on 2017. 5. 23..
 */
class VersionData {
    @SerializedName("timestamp")
    @Expose
    var timestamp : String?= null

    @SerializedName("data")
    @Expose
    var versionInfo : VersionInfo?= null
    class VersionInfo{
        @SerializedName("is_force_update")
        @Expose
        var is_force_update : String?= null
        @SerializedName("version")
        @Expose
        var version : String?= null
    }
}