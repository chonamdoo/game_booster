package com.couchgram.gamebooster.data.source.boostapp

import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.data.source.BoostAppInfo
import io.reactivex.Single

/**
 * Created by chonamdoo on 2017. 4. 27..
 */
interface BoostAppSource {
    fun getMaxPosition(): Int
    fun getInstalledAppList() : Single<ArrayList<AppInfo>>
    fun addBoostApp(appInfo: AppInfo)
    fun removeBoostApp(packageName: String)
    fun hasBoostApp(packageName: String) : Boolean
    fun getBoostAppList() : Single<ArrayList<BoostAppInfo>>
    fun updateBoostAppAdTime(packageName: String,addTime: String)
    fun updatePosition(packageName: String,position: Int)
    fun updatePositions(boostAppInfoList : List<BoostAppInfo>)
}