package com.couchgram.gamebooster

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import com.couchgram.gamebooster.util.fresco.imagepipeline.ImagePipelineConfigFactory
import com.couchgram.gamebooster.util.timber.CrashReportTree
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.instacart.library.truetime.TrueTimeRx
import com.marcinmoskala.kotlinpreferences.PreferenceHolder
import com.mobvista.msdk.out.MobVistaSDKFactory
import com.threshold.rxbus2.RxBus
import timber.log.Timber
import io.reactivex.schedulers.Schedulers
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by chonamdoo on 2017. 4. 27..
 */
class GameBoostApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppContext.init(base)
    }

    fun init() {
        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(applicationContext, Crashlytics.Builder().core(core).build(), Crashlytics())
        Stetho.initializeWithDefaults(applicationContext)
        PreferenceHolder.setContext(applicationContext)

        frescoInit()
//        trueTimeInit()
        RxBus.config(AndroidSchedulers.mainThread())
        initMobvista()
    }

    fun frescoInit() {
        Fresco.initialize(applicationContext, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(applicationContext))
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportTree())
        }
    }

    fun trueTimeInit() {
        TrueTimeRx.build()
                .withConnectionTimeout(31_428)
                .withRetryCount(20)
                .withSharedPreferences(this)
                .withLoggingEnabled(true)
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ date ->
                    LogUtils.v("DEBUG700", "trueTimeInit Date : ${date.time}")
                }, { throwable ->
                    LogUtils.v("DEBUG700", "trueTimeInit error : ${throwable.printStackTrace()}")
                })
    }

    fun initMobvista() {
        val sdk = MobVistaSDKFactory.getMobVistaSDK()
        val map = sdk.getMVConfigurationMap(getString(R.string.mobvista_app_id), getString(R.string.mobvista_api_key))
        sdk.init(map, this)
    }

}