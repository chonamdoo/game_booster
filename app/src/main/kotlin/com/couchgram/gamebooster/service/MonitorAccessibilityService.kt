package com.couchgram.gamebooster.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.annotation.TargetApi
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityWindowInfo
import com.couchgram.gamebooster.data.AccessbilityAppEvent
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import com.threshold.rxbus2.RxBus

/**
 * Created by chonamdoo on 2017. 5. 25..
 */

class MonitorAccessibilityService : AccessibilityService(){
    override fun onCreate() {
    }
    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if(Utils.isServiceOpen(this,BoostService::class.java)){
                if(it.packageName != null && it.className != null){
                    LogUtils.v("DEBUG900","packageName : ${it.packageName}, ${it.className} , ${it.eventType}, isFullScreen : ${it.isFullScreen}")

                    if(Pref.call_window_mode){
                        when(it.eventType){
                            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED ->{
                                if(Utils.checkDialerApp(it.packageName.toString())) {
                                    RxBus.getInstance().post(AccessbilityAppEvent(it.packageName.toString(), it.className.toString()))
                                }
                            }
                            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ->{
                                if(checkEventType(it)) {
                                    RxBus.getInstance().post(AccessbilityAppEvent(it.packageName.toString(), it.className.toString()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onServiceConnected() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getWindowType(accessibilityEvent: AccessibilityEvent): Int {
        windows
                .filter { accessibilityEvent.windowId == it.id }
                .forEach { return it.type }
        return AccessibilityWindowInfo.TYPE_APPLICATION
    }

    private fun checkEventType(event: AccessibilityEvent): Boolean {
        return ((event.packageName != "android" && event.packageName != packageName) &&
                !Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD).startsWith(event.packageName) &&
                !(event.packageName.matches("com\\.(google\\.)?android\\.systemui".toRegex())) &&
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getWindowType(event) == AccessibilityWindowInfo.TYPE_APPLICATION)) ||
                Utils.checkDialerApp(event.packageName.toString())
    }
}