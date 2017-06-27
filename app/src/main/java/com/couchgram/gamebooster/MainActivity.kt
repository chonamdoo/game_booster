package com.couchgram.gamebooster


import android.content.ComponentName
import android.content.Intent
import android.content.pm.LabeledIntent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.couchgram.gamebooster.data.AccessbilityAppEvent
import com.couchgram.gamebooster.data.CallSate
import com.couchgram.gamebooster.ui.widget.listener.SettingMenuClickListener
import com.couchgram.gamebooster.util.LogUtils
import com.mobvista.msdk.MobVistaConstans
import com.mobvista.msdk.out.MobVistaSDKFactory
import io.reactivex.Flowable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*TrueTimeRx.build()
                .withConnectionTimeout(31428)
                .withRetryCount(100)
                .withSharedPreferences(this)
                .withLoggingEnabled(true)
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ date ->
                    LogUtils.v("DEBUG700", "date :" + date.time) },
                        { throwable -> LogUtils.v("DEBUG700", "throwable :" + throwable.message) })
                { LogUtils.v("DEBUG700", "complete :") }*/
        /* BoostAppRepository.instance.getInstalledAppList().subscribeOn(Schedulers.io())
                 .subscribe(object : Observer<ArrayList<AppInfo>> {
                     override fun onSubscribe(@NonNull d: Disposable) {

                     }

                     override fun onNext(@NonNull appInfos: ArrayList<AppInfo>) {
                         Log.v("DEBUG600","appInfos : ${appInfos[0].appName}")
                     }

                     override fun onError(@NonNull e: Throwable) {

                     }

                     override fun onComplete() {

                     }
                 })
 */
        /*BoostAppRepository.instance.getInstalledAppList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }*/
        /*val excutor = CustomScheduledExecutor("test",false)
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG100","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG101","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG102","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG103","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG104","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG105","${i}")
            }
        })
        excutor.submit(Runnable {
            for (i in 0..999) {
                Log.v("DEBUG106","${i}")
            }
        })*/
    }

}
