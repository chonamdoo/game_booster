package com.couchgram.gamebooster.util

import android.content.Intent
import android.content.pm.PackageManager
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.data.preference.Pref
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.*
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context.ACCESSIBILITY_SERVICE
import android.content.pm.LabeledIntent
import android.view.accessibility.AccessibilityManager
import com.couchgram.gamebooster.service.MonitorAccessibilityService
import android.telecom.PhoneAccount
import android.net.Uri
import android.provider.Telephony
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationManagerCompat
import android.telecom.TelecomManager
import com.couchgram.gamebooster.R
import ezy.assist.compat.SettingsCompat
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by chonamdoo on 2017. 5. 4..
 */

object Utils {
    fun setAdvertiseID() {
        if (Pref.advertiseId.isNullOrEmpty()) {
            Completable.fromAction {
                try {
                    val info = AdvertisingIdClient.getAdvertisingIdInfo(AppContext.getInstance())
                    info?.let {
                        Pref.advertiseId = info.id
                    }
                } catch (e: Exception) {

                }
            }.subscribeOn(Schedulers.io()).subscribe()
        }
    }

    fun currentDate(): Date {
        if (TrueTimeRx.isInitialized()) {
            return TrueTimeRx.now()
        }
        return Date()
    }

    fun currentTimeMillis() = System.currentTimeMillis()

    fun getPackageName(): String {
        if (Pref.packageName.isNullOrEmpty()) {
            Pref.packageName = AppContext.getInstance().packageName
        }
        return Pref.packageName
    }

    fun getVersionName() = try {
        val packageMngr = AppContext.getInstance().packageManager
        val packageInfo = packageMngr.getPackageInfo(AppContext.getInstance().packageName, 0)
        packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    fun isVersionDownloadableNewer(versionDownloadable: String): Boolean {
        return getVersionName()?.let {
            it != versionDownloadable && versionCompareNumerically(versionDownloadable, it) > 0
        } ?: false
    }

    private fun versionCompareNumerically(versionDownloadable: String, versionInstalled: String): Int {
        val vals1 = versionDownloadable.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vals2 = versionInstalled.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }
        try {
            // compare first non-equal ordinal number
            if (i < vals1.size && i < vals2.size) {
                val diff = Integer.valueOf(vals1[i])!!.compareTo(Integer.valueOf(vals2[i]))
                return Integer.signum(diff)
            } else {
                return Integer.signum(vals1.size - vals2.size)
            }// the strings are equal or one string is a substring of the other
            // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        } catch (e: NumberFormatException) {
            // Possibly there are different versions of the app in the store, so we can't check.
            return 0
        }
    }

    fun lunchApp(packageName: String) {
        try {
            AppContext.getInstance().packageManager.getLaunchIntentForPackage(packageName)?.run {
                addCategory(Intent.CATEGORY_LAUNCHER)
                AppContext.getInstance().startActivity(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isPackageInstalled(packageName: String) = try {
        (AppContext.getInstance().packageManager.getLaunchIntentForPackage(packageName) != null)
    } catch (e: Exception) {
        false
    }


    fun isAccessbillitySetting(): Boolean {
        var isAccessbillitySetting = false
        val packageManager = AppContext.getInstance().packageManager
        val infoList = packageManager.queryIntentActivities(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), PackageManager.MATCH_DEFAULT_ONLY)
        if (!infoList.isEmpty()) {
            isAccessbillitySetting = infoList[0].activityInfo.exported
        }
        return isAccessbillitySetting
    }


    fun reqAccessibilityPerms(activity: Activity) {
        activity.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply { flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NO_ANIMATION})
    }


    fun reqAccessibilityHomeIntent(activity: Activity){
        activity.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            action = Intent.ACTION_MAIN
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NO_ANIMATION
            addCategory(Intent.CATEGORY_HOME)
        })
    }

    fun reqOverlayHomeIntent(activity: Activity){
        var intent_action = "android.settings.action.MANAGE_OVERLAY_PERMISSION"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            intent_action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        }
        activity.startActivity(Intent(intent_action).apply {
            action = Intent.ACTION_MAIN
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            addCategory(Intent.CATEGORY_HOME)
        })
    }


    fun isNotificationManagerSetting() : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notiManager = AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notiManager.isNotificationPolicyAccessGranted
        }
        return true
    }

    fun isNotificationListenerSetting() : Boolean{
        val notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(AppContext.getInstance())
        val myPackageName = getPackageName()
        return notiListenerSet.any { it != null && it == myPackageName }
    }

    fun getDefaultHomeLauncher(): String {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        val defaultLauncher = AppContext.getInstance().packageManager.resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (defaultLauncher != null && defaultLauncher.activityInfo != null && defaultLauncher.activityInfo.packageName.isNotEmpty()) {
            return defaultLauncher.activityInfo.packageName
        }
        return ""
    }

    fun isServiceOpen(context: Context,clazz: Class<out Service>): Boolean {
        val am = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = am
                .getRunningServices(Integer.MAX_VALUE)
        return runningServices
                .map {
                    it.service.className
                }
                .any { it == clazz.name }
    }

    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val service = getPackageName() + "/" + MonitorAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    AppContext.getInstance().applicationContext.contentResolver,
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
//            LogUtils.v("DEBUG700", "accessibilityEnabled = " + accessibilityEnabled)
        } catch (e: Settings.SettingNotFoundException) {
//            LogUtils.v("DEBUG700", "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
//            LogUtils.v("DEBUG700", "***ACCESSIBILITY IS ENABLED*** -----------------")
            val settingValue = Settings.Secure.getString(
                    AppContext.getInstance().applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()

//                    LogUtils.v("DEBUG700", "-------------- > accessibilityService :: $accessibilityService $service")
                    if (accessibilityService.equals(service, ignoreCase = true)) {
//                        LogUtils.v("DEBUG700", "We've found the correct setting - accessibility is switched on!")
                        return true
                    }
                }
            }
        } else {
            LogUtils.v("DEBUG700", "***ACCESSIBILITY IS DISABLED***")
        }

        return false
    }

    fun checkEnabledAccessibilityService(): Boolean {
        if (isAccessibilitySettingsOn())
            return true
        val accessibilityServices = (AppContext.getInstance().getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager)
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return accessibilityServices.any { it.id == AppContext.getInstance().packageName+"/com.couchgram.gamebooster.service/MonitorAccessibilityService" }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getDefaultDailer(context: Context) : String?{
        val tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return tm.defaultDialerPackage
    }
    fun getDefaultDialerApplication(context: Context): String? {
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
            return getDefaultDailer(context)
        }
        val defaultPackageName = Settings.Secure.getString(context.contentResolver,"dialer_default_application")
        val packageNames = getInstalledDialerApplications(context)
        LogUtils.v("DEBUG800","packageNames : $packageNames")
        if (packageNames.contains(defaultPackageName)) {
            return defaultPackageName
        }

        val systemDialerPackageName = "com.android.dialer"

        if (TextUtils.isEmpty(systemDialerPackageName)) {
            return null
        }

        if (packageNames.contains(systemDialerPackageName)) {
            return systemDialerPackageName
        } else {
            return null
        }
    }

    fun getInstalledDialerApplications(context: Context): List<String> {

        val intent = Intent(Intent.ACTION_DIAL)
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)

        val packageNames = arrayListOf<String>()

        resolveInfoList
                .map { it.activityInfo }
                .filter {
                    it != null && !packageNames.contains(it.packageName)
                }
                .forEach {
                    packageNames.add(it.packageName)
                }

        val dialIntentWithTelScheme = Intent(Intent.ACTION_DIAL)
        dialIntentWithTelScheme.data = Uri.fromParts(PhoneAccount.SCHEME_TEL, "", null)
        return filterByIntent(context, packageNames, dialIntentWithTelScheme)
    }
    private fun filterByIntent(context: Context, packageNames: List<String>?, intent: Intent): List<String> {
        if (packageNames == null || packageNames.isEmpty()) {
            return ArrayList()
        }

        val result = ArrayList<String>()
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
        val length = resolveInfoList.size
        (0..length - 1)
                .map { resolveInfoList[it].activityInfo }
                .filter { it != null && packageNames.contains(it.packageName) && !result.contains(it.packageName) }
                .forEach { result.add(it.packageName) }

        return result
    }

    fun drawOverlayPermissionChecker(): Observable<Long>{
        return Observable.interval(500, TimeUnit.MILLISECONDS)
                .map { it-> it * 500 }
                .takeUntil {
                    SettingsCompat.canDrawOverlays(AppContext.getInstance()) || it >= 5 * 60 * 1000
                }
    }

    fun accessibilityPermissionChecker(): Observable<Long>{
        return Observable.interval(500,TimeUnit.MILLISECONDS)
                .map { it-> it * 500 }
                .takeUntil {
                    Utils.checkEnabledAccessibilityService() || it >= 5 * 60 * 1000
                }
    }

    fun shareSns(context : Context) {

        val snsList =  arrayListOf("com.facebook.katana","com.twitter.android","com.google.android.apps.plus","com.kakao.talk",
                "jp.naver.line.android","com.facebook.orca","com.instagram.android","com.whatsapp","com.tencent.mm","org.telegram.messenger","com.futurebits.instamessage.free")
        val message = context.getString(R.string.privacy_lbl_share_sns)
        val smsIntent : Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(AppContext.getInstance())
            smsIntent = Intent(Intent.ACTION_SEND)
            smsIntent.type = "text/plain"
            smsIntent.putExtra(Intent.EXTRA_TEXT,message)

            if (defaultSmsPackageName != null) {
                smsIntent.`package` = defaultSmsPackageName
            }

        } else {
            smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.data = Uri.parse("sms:")
            smsIntent.putExtra("sms_body",message)
        }

        val queryIntent = Intent(Intent.ACTION_SEND)
        queryIntent.type = "text/plain"

        val resolveInfos = AppContext.getInstance().packageManager.queryIntentActivities(queryIntent, 0)

        val otherAppIntentList = ArrayList<LabeledIntent>()
        for (i in resolveInfos.indices) {
            val ri = resolveInfos[i]
            val packageName = ri.activityInfo.packageName
            val intentToAdd = Intent()
            if (snsList.contains(packageName)) {
                intentToAdd.component = ComponentName(packageName, ri.activityInfo.name)
                intentToAdd.action = Intent.ACTION_SEND
                intentToAdd.type = "text/plain"
                intentToAdd.`package` = packageName
                intentToAdd.putExtra(Intent.EXTRA_TEXT, message)

                otherAppIntentList.add(LabeledIntent(intentToAdd, packageName,ri.loadLabel(AppContext.getInstance().packageManager), ri.icon))
            }
        }

        val extraIntents = otherAppIntentList.toTypedArray()

        val chooserIntent = Intent.createChooser(smsIntent,context.getString(R.string.share_title))

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppContext.getInstance().startActivity(chooserIntent)
    }

    fun isIntentAvailable(intent: Intent): Boolean {
        val list = AppContext.getInstance().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }
    fun isSamsungDialer() : Boolean{
        return Build.MANUFACTURER.equals("samsung",ignoreCase = true) &&
               Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Utils.getDefaultDialerApplication(AppContext.getInstance())?.contains("com.samsung.android.contacts",ignoreCase = true)?:false
//
    }

    fun checkDialerApp(event: String) : Boolean{
        return  event.matches("com\\.(samsung\\.)?android\\.incallui".toRegex()) ||
                event.matches("com\\.(google\\.)?android\\.dialer".toRegex()) ||
                event.matches(".*dialer.*".toRegex()) ||
                event.matches(".*incallui.*".toRegex())
//                event.matches("com\\.(google\\.)?android\\.systemui".toRegex()) ||
    }

}