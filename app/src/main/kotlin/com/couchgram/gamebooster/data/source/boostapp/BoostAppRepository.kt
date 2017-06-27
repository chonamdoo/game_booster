package com.couchgram.gamebooster.data.source.boostapp

import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.data.source.boostapp.local.BoostAppLocalSource
import io.reactivex.Single

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class BoostAppRepository
private constructor(val boostLocalAppSource: BoostAppSource) : BoostAppSource {

    override fun getInstalledAppList(): Single<ArrayList<AppInfo>> {
        return boostLocalAppSource.getInstalledAppList()
    }

    override fun addBoostApp(appInfo: AppInfo) {
        boostLocalAppSource.addBoostApp(appInfo)
    }

    override fun removeBoostApp(packageName: String) {
        boostLocalAppSource.removeBoostApp(packageName)
    }

    override fun hasBoostApp(packageName: String) :Boolean {
        return boostLocalAppSource.hasBoostApp(packageName)
    }

    override fun getBoostAppList(): Single<ArrayList<BoostAppInfo>> {
        return boostLocalAppSource.getBoostAppList()
    }

    override fun updateBoostAppAdTime(packageName: String, addTime: String) {
        boostLocalAppSource.updateBoostAppAdTime(packageName,addTime)
    }

    override fun updatePosition(packageName: String, position: Int) {
        boostLocalAppSource.updatePosition(packageName,position)
    }

    override fun updatePositions(boostAppInfoList : List<BoostAppInfo>) {
        boostLocalAppSource.updatePositions(boostAppInfoList)
    }

    override fun getMaxPosition(): Int {
        return boostLocalAppSource.getMaxPosition()
    }

    companion object{
        val instance: BoostAppRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            BoostAppRepository(BoostAppLocalSource.instance)
        }
        /*private var INSTANCE : BoostAppRepository? = null
        val instance: BoostAppRepository
            get() {
                if (INSTANCE == null) {
                    INSTANCE = BoostAppRepository(BoostAppLocalSource.instance)
                }
                return INSTANCE as BoostAppRepository
            }*/

    }
}