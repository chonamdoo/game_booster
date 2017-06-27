package com.couchgram.gamebooster.service


import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.task.ProcessScan
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.util.concurrent.TimeUnit
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.Uri
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.call.PhoneCallStateReceiver
import com.couchgram.gamebooster.data.AccessbilityAppEvent
import com.couchgram.gamebooster.data.CallSate
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.ui.callinfo.CallInfo
import com.couchgram.gamebooster.ui.widget.dialog.ConfirmDialogActivity
import com.couchgram.gamebooster.util.*
import com.crashlytics.android.Crashlytics
import com.threshold.rxbus2.RxBus
import com.threshold.rxbus2.annotation.RxSubscribe
import com.threshold.rxbus2.util.EventThread
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

/**
 * Created by chonamdoo on 2017. 4. 28..
 */

class BoostService : Service(){
    var boostAppInfo : BoostAppInfo?= null
    val compositDisposable = CompositeDisposable()
    var notiBuilder : NotificationCompat.Builder?= null
    val phoneCallStateReceiver : PhoneCallStateReceiver by lazy {
        PhoneCallStateReceiver()
    }
    val telephonyManager : TelephonyManager by lazy {
        getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }
    val audioManager : AudioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val keyEventReceiver : KeyEventReceiver by lazy {
        KeyEventReceiver()
    }
    val appCheckPublish = PublishProcessor.create<AccessbilityAppEvent>()
    var callInfo : CallInfo?= null
    var currentVolume : Int = -1
    var currentRingerMode : Int = 0
    var eventHash = hashSetOf<String>()

    private lateinit var disposable : Disposable
    override fun onCreate() {
        super.onCreate()
        registerPhoneCallReceiver()
        registerKeyEventReceiver()
        initIntervalBoost()
        RxBus.getInstance().register(this)
        trackingAppEvent()
        getCurrentRingerMode()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flag: Int, startId: Int): Int {
        intent?.let {
            it.action?.let {
                if(it == Constants.NOTI_ACTION_EXIT_BOOST_SERVICE){
                    stopSelf()
                }else if(it == Constants.CHECK_DESTROY_BOOST_SERVICE){
                    Intent(this, ConfirmDialogActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                        putExtra(Constants.BOOST_ITEM, boostAppInfo)
                        startActivity(this)
                    }
                }
            }?:it.extras?.let {
                it.get(Constants.BOOST_ITEM)?.let {
                    boostAppInfo = it as BoostAppInfo
                    if(notiBuilder == null) {
                        initNotification(it)
                    }else{
                        updateNotification(it)
                    }
                }

            }

        }
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        unRegisterPhoneCallReceiver()
        unRegisterKeyEventReceiver()
        compositDisposable.dispose()
        RxBus.getInstance().unregister(this)
        hideCallInfoView()
    }
    fun getCurrentRingerMode(){
        currentRingerMode = audioManager.ringerMode
    }

    fun initIntervalBoost(){
        disposable = Observable.interval(5, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val processScan = ProcessScan(this)
                    processScan.cleanTask(processScan.runningTaskList())
                }
                .subscribeWith(object : DisposableObserver<Long>() {
                    override fun onNext(@NonNull aLong: Long) {
                        if(Pref.isDevelopMode){
                            Toast.makeText(AppContext.getInstance(),"앱 클린 사이즈 : ${FileUtils.getFormatMemSize(aLong)}",Toast.LENGTH_SHORT).show()
                        }
                        LogUtils.v("DEBUG900","앱 클린 사이즈 : $aLong")
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
        compositDisposable.add(disposable)

    }
    fun initNotification(boostAppInfo: BoostAppInfo){
        try {
            isShowDestroyBoosterPopUp = false
            homeKeyEvent = false
            notiBuilder = NotificationCompat.Builder(this)
                    .apply {
                        setSmallIcon(if (OsVersions.isAtLeastLollipop()) R.mipmap.l_launcher else R.mipmap.l_launcher)
                        color = Color.parseColor("#7cd7f9")
                        priority = NotificationCompat.PRIORITY_MAX
                        setAutoCancel(false).setOngoing(true)
                        setContent(getComplexNotificationView(boostAppInfo))
                    }
            startForeground(1234, notiBuilder?.build())

        }catch (e : Exception){
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    fun updateNotification(boostAppInfo: BoostAppInfo){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiBuilder!!.setContent(getComplexNotificationView(boostAppInfo))
        notificationManager.notify(1234,notiBuilder!!.build())
        compositDisposable.clear()
        initIntervalBoost()
        trackingAppEvent()
    }

    private fun getComplexNotificationView(boostAppInfo: BoostAppInfo): RemoteViews {
        val notificationView = RemoteViews(packageName, R.layout.boost_noti)
        updateNotiView(notificationView,boostAppInfo)
        notificationView.setOnClickPendingIntent(R.id.exit_boost_service,getPendingIntent(R.id.exit_boost_service,Constants.NOTI_ACTION_EXIT_BOOST_SERVICE))
        return notificationView
    }
    private fun updateNotiView(notificationView : RemoteViews,boostAppInfo: BoostAppInfo){
        notificationView.setImageViewBitmap(R.id.app_icon,drawableToBitmap(packageManager.getApplicationIcon(boostAppInfo.packageName)))
        notificationView.setTextViewText(R.id.boost_app_name,getString(R.string.noti_boosting_desc,boostAppInfo.appName))
    }
    private fun getPendingIntent(id: Int, action: String) = PendingIntent.getService(this, id, Intent(this,BoostService::class.java).apply { setAction(action) }, 0)

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun registerPhoneCallReceiver(){
        registerReceiver(phoneCallStateReceiver, IntentFilter().apply {
            addAction("android.intent.action.PHONE_STATE")
            addAction("android.intent.action.PHONE_STATE_EXT")
            addAction("android.intent.action.PHONE_STATE_2")
            addAction("android.intent.action.PHONE_STATE2")
            addAction("android.intent.action.DUAL_PHONE_STATE")
//            addAction("android.intent.action.SUBSCRIPTION_PHONE_STATE")
//            addAction("android.intent.action.NEW_OUTGOING_CALL")
            priority = Int.MAX_VALUE
        })
    }

    fun unRegisterPhoneCallReceiver(){
        try{
            unregisterReceiver(phoneCallStateReceiver)
        }catch (e : Exception){

        }
    }
    fun updateCallInfoView(event: CallSate){
        callInfo?.let {
            hideCallInfoView()
        }
        callInfo = CallInfo(AppContext.getInstance(),event).apply {
            showCallInfo()
            setDisconnectCallClickListener(View.OnClickListener {

                if(Pref.call_window_mode) {
                    PhoneUtils.disconnectCall()
                }
                hideCallInfoView()
            })
        }
    }
    fun showCallInfoView(event: CallSate){
        callInfo?.let {
            hideCallInfoView()
        }
        callInfo = CallInfo(AppContext.getInstance(),event).apply {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            showCallInfo()
            setAcceptCallClickListener(View.OnClickListener {
                if(Pref.call_window_mode){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_PLAY_SOUND)
                    hideCallInfoView()
                    PhoneUtils.acceptCall()
                }else{
                    hideCallInfoView()
                    context.startActivity(Intent("android.intent.action.CALL", Uri.parse("tel:${event.phoneNumber}")))
                }
            })
            setDisconnectCallClickListener(View.OnClickListener {

                if(Pref.call_window_mode) {
                    PhoneUtils.disconnectCall()
                }
                hideCallInfoView()
            })
        }
    }
    fun hideCallInfoView(){
        callInfo?.hideCallInfo()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onPhoneCallEvent(event : CallSate){
        LogUtils.v("DEBUG800","onPhoneCallEvent event: ${event.state} ,isSamsungDialer() : ${Utils.isSamsungDialer()}")
        if(Pref.call_window_mode) {
            if (event.state == Constants.ON_CALL && !Utils.isSamsungDialer()) {
                compositDisposable.add(Single.timer(2000,TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            LogUtils.v("DEBUG800","checkDialerApp : ${checkDialerHeaderNoti()}, eventHash : $eventHash")
                            if(checkDialerHeaderNoti() && telephonyManager.callState == TelephonyManager.CALL_STATE_RINGING){
                                showCallInfoView(event)
                            }
                        },{}))
            }else if(event.state == Constants.ACCEPT_CALL){
                hideCallInfoView()
                updateCallInfoView(event)
                eventHash.clear()
            }else if(event.state == Constants.END_CALL){
                eventHash.clear()
                if(currentVolume != -1) {
                    compositDisposable.add(Observable.interval(500, TimeUnit.MILLISECONDS)
                            .map { it -> it * 500 }
                            .takeUntil {
                                (currentVolume == audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) || it >= 2 * 1000
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND)
                            }, {}, {}))
                }
                hideCallInfoView()
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.TRAMPOLINE)
    fun onAccessibilityEvent(event : AccessbilityAppEvent){
        LogUtils.v("DEBUG800","onAccessibilityEvent")
        eventHash.add(event.eventPackageName)
        appCheckPublish.onNext(event)
    }

    fun trackingAppEvent(){
        compositDisposable.add(appCheckPublish.ofType(AccessbilityAppEvent::class.java)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val callState = telephonyManager.callState
                    LogUtils.v("DEBUG800", "other app :${it.eventPackageName} , callState : $callState")
                    if(it.eventPackageName != boostAppInfo!!.packageName && (callState == TelephonyManager.CALL_STATE_RINGING || callState == TelephonyManager.CALL_STATE_OFFHOOK)){
                        LogUtils.v("DEBUG800","전화 오고 게임 화면이 바뀔때 : ${it.eventPackageName}")
                        try{
                            packageManager.getLaunchIntentForPackage(boostAppInfo!!.packageName).apply {
                                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                addFlags(Intent.FLAG_FROM_BACKGROUND)
                                addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
                                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                startActivity(this)
                            }
                        }catch (e : Exception){
                        }

                    }else if(it.eventPackageName == Utils.getDefaultHomeLauncher() && (callState != TelephonyManager.CALL_STATE_RINGING)){
                        if(!homeKeyEvent && !BoostService.isShowDestroyBoosterPopUp) {
                            Intent(this, ConfirmDialogActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                                putExtra(Constants.BOOST_ITEM, boostAppInfo)
                                startActivity(this)
                            }
                        }
                    }else if(it.eventPackageName == boostAppInfo!!.packageName &&  homeKeyEvent){
//                        LogUtils.v("DEBUG800","홈키 눌렀다 다시 앱 들어올때 : $it")
                        BoostService.homeKeyEvent = false
                        initIntervalBoost()
                    }
                },{LogUtils.v("DEBUG800","에러: ${it.message}")}))
    }

    fun checkDialerHeaderNoti() : Boolean{
       return !eventHash.filter { (Utils.checkDialerApp(it))}.isNotEmpty()
    }
    fun registerKeyEventReceiver() {
        registerReceiver(keyEventReceiver, IntentFilter(Constants.CLOSE_SYSTEM_DIALOGS))
    }

    fun unRegisterKeyEventReceiver() {
        try {
            unregisterReceiver(keyEventReceiver)
        } catch (e : Exception) {
        }

    }
    inner class KeyEventReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (TextUtils.equals(intent.action, Constants.CLOSE_SYSTEM_DIALOGS)) {
                val keyEvent = intent.getStringExtra("reason")
                if (!TextUtils.isEmpty(keyEvent) && (keyEvent == "homekey")) {
                    homeKeyEvent = true
                    eventHash.clear()
                    compositDisposable.remove(disposable)
                }
            }
        }
    }
    companion object{
        var isShowDestroyBoosterPopUp = false
        var homeKeyEvent = false
    }
}
