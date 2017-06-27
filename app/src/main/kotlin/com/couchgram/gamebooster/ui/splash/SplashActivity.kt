package com.couchgram.gamebooster.ui.splash

import android.content.Intent
import android.os.Bundle
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.api.Api
import com.couchgram.gamebooster.service.MonitorNotificationService
import com.couchgram.gamebooster.ui.boostapp.BoostAppListActivity
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.RootUtil
import com.couchgram.gamebooster.util.RxSchedulers
import com.couchgram.gamebooster.util.Utils
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit



/**
 * Created by chonamdoo on 2017. 5. 17..
 */

class SplashActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Api.apiReqService.reqVersionInfo()
                .compose(RxSchedulers.io_main())
                .subscribe({
                    versionData->
                     LogUtils.v("DEBUG700","versionData :${versionData?.versionInfo?.version}")
                },{})

        Single.timer(1500,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(this,ActivityEvent.DESTROY)
                .subscribe({
                    startActivity(Intent(this,BoostAppListActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    finish()
                },{})
    }

    override fun onDestroy() {
        super.onDestroy()
        animation_view.cancelAnimation()
        animation_view.clearAnimation()

    }
}