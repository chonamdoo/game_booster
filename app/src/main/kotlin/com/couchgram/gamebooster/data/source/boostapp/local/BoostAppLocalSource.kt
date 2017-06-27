package com.couchgram.gamebooster.data.source.boostapp.local

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.data.source.boostapp.BoostAppSource
import com.couchgram.gamebooster.db.BoosterDbHelper
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import io.reactivex.Single

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class BoostAppLocalSource
private constructor(): BoostAppSource {
    private val db = BoosterDbHelper(AppContext.getInstance()).writableDatabase

    override fun getInstalledAppList() :Single<ArrayList<AppInfo>> {
        try{
            return Single.defer {
                val appInfoList = arrayListOf<AppInfo>()
                val pm = AppContext.getInstance().packageManager
                val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
                for (packageInfo in packages) {
                    val applicationInfo = packageInfo.applicationInfo
                    if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != ApplicationInfo.FLAG_SYSTEM
                            && packageInfo.packageName != AppContext.getInstance().packageName
                            && !hasBoostApp(packageInfo.packageName)) {
                        val info = AppInfo(applicationInfo.loadLabel(pm) as String,packageInfo.packageName,applicationInfo.loadIcon(pm),hasBoostApp(packageInfo.packageName))
                        appInfoList.add(info)
                    }
                }
                appInfoList.sortWith(compareBy { it.appName })
                Single.just(appInfoList)
            }
        }catch (e : Exception){
            return Single.just(arrayListOf<AppInfo>())
        }
    }

    override fun addBoostApp(appInfo: AppInfo) = try{
        db.execSQL("insert into "+BoosterDbHelper.TABLE_NAME + "(" + BoosterDbHelper.COLUMN_NAME_APP_NAME +", "+BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME+", "+BoosterDbHelper.COLUMN_NAME_ADD_TIME +", "+BoosterDbHelper.COLUMN_NAME_ADD_POSITION +") values (?,?,?,?)", arrayOf(appInfo.appName,appInfo.packageName,Utils.currentTimeMillis(),getMaxPosition()))
    }catch (e : Exception){

    }

    override fun removeBoostApp(packageName: String) = try {
        db.execSQL("delete from "+BoosterDbHelper.TABLE_NAME + " where "+BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME +" ='"+packageName+"'")
    }catch (e : Exception){

    }

    override fun updateBoostAppAdTime(packageName: String, addTime: String) = try {
        db.execSQL("update "+BoosterDbHelper.TABLE_NAME + " set " +BoosterDbHelper.COLUMN_NAME_ADD_TIME+" ='"+addTime+"' where "+BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME +"='"+packageName+"'")
    }catch (e : Exception){

    }

    override fun updatePosition(packageName: String, position: Int) = try{
        db.execSQL("update "+BoosterDbHelper.TABLE_NAME + " set " +BoosterDbHelper.COLUMN_NAME_ADD_POSITION+" ="+position+" where "+BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME +"='"+packageName+"'")
    }catch (e : Exception){

    }

    override fun updatePositions(boostAppInfoList: List<BoostAppInfo>) = try{
        db.beginTransaction()
        for (i in 0..boostAppInfoList.size - 2) {
            db.execSQL("update "+BoosterDbHelper.TABLE_NAME + " set " +BoosterDbHelper.COLUMN_NAME_ADD_POSITION+" ="+(i+1)+" where "+BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME +"='"+boostAppInfoList[i].packageName+"'")
        }
        db.setTransactionSuccessful()
    }catch (e : Exception){
    }finally {
        db.endTransaction()
    }

    override fun getMaxPosition() = try{
        val cursor = db.query(BoosterDbHelper.TABLE_NAME, arrayOf("max("+BoosterDbHelper.COLUMN_NAME_ADD_POSITION+") as "+BoosterDbHelper.COLUMN_NAME_ADD_POSITION),null, null,null,null,null)
        cursor?.use {
            while(it.moveToNext()){
                val pos = it.getInt(it.getColumnIndexOrThrow(BoosterDbHelper.COLUMN_NAME_ADD_POSITION))
                return pos+1
            }
            -1
        } ?: -1
    }catch (e : Exception){
        -1
    }

    override fun hasBoostApp(packageName: String) = try{
        val cursor = db.query(BoosterDbHelper.TABLE_NAME, arrayOf(BoosterDbHelper._ID),BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME +" =?", arrayOf(packageName),null,null,null)
        cursor?.use {
            it.count > 0
        } ?: false
    }catch (e : Exception){
        false
    }

    override fun getBoostAppList() : Single<ArrayList<BoostAppInfo>> {
        try{
            val appInfoList = arrayListOf<BoostAppInfo>()
            return Single.defer {
                val cursor = db.query(BoosterDbHelper.TABLE_NAME, arrayOf(BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME,BoosterDbHelper.COLUMN_NAME_APP_NAME,BoosterDbHelper.COLUMN_NAME_ADD_TIME),null,null,null,null,BoosterDbHelper.COLUMN_NAME_ADD_POSITION +" asc")
                cursor?.use {
                    while (it.moveToNext()) {
                        val packageName = it.getString(it.getColumnIndexOrThrow(BoosterDbHelper.COLUMN_NAME_APP_PACKAGE_NAME))
                        if(Utils.isPackageInstalled(packageName)) {
                            val info = BoostAppInfo(packageName, it.getString(it.getColumnIndexOrThrow(BoosterDbHelper.COLUMN_NAME_APP_NAME)), false, it.getString(it.getColumnIndexOrThrow(BoosterDbHelper.COLUMN_NAME_ADD_TIME)))
                            appInfoList.add(info)
                        }else{
                            removeBoostApp(packageName)
                        }
                    }
                }
                appInfoList.add(BoostAppInfo("boost_ad","boost_add",true))
                Single.just(appInfoList)
            }
        }catch (e : Exception){
            return Single.just(arrayListOf<BoostAppInfo>())
        }
    }

    companion object{
        val instance : BoostAppLocalSource by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            BoostAppLocalSource()
        }
        /*private var INSTANCE : BoostAppLocalSource? = null
        val instance: BoostAppLocalSource
            get() {
                if (INSTANCE == null) {
                    INSTANCE = BoostAppLocalSource()
                }
                return INSTANCE as BoostAppLocalSource
            }*/

    }
}