package com.couchgram.gamebooster.api

import io.reactivex.Single
import retrofit2.http.*
import java.util.ArrayList

/**
 * Created by chonamdoo on 2017. 5. 4..
 */
interface RequestService {
    @GET("/v1/campaign")
    fun reqGetAdsList(@QueryMap param: Map<String, String>): Single<ArrayList<AdsData>>

    @GET("/v1/click")
    fun reqTracker(@Query("vargs")vargs : String) : Single<TrackerData>

    @GET("/gameBoosterVersionIfo")
    fun reqVersionInfo() : Single<VersionData>
}