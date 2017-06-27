package com.couchgram.gamebooster.util

import android.content.Intent
import android.content.ComponentName
import android.net.Uri
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.ui.shortcut.GameBoxShortcutActivity
import android.text.TextUtils
import android.content.pm.PackageManager
import com.couchgram.gamebooster.AppContext


/**
 * Created by chonamdoo on 2017. 5. 2..
 */
object ShortCutUtils {
    fun createGameBoxActivityShortcut() {
        installShortcut(ComponentName(AppContext.getInstance(), GameBoxShortcutActivity::class.java),AppContext.getInstance().getString(R.string.game_folder))
    }
    fun deleteGameBoxActivityShortcut() {
        uninstallShortcut(ComponentName(AppContext.getInstance(), GameBoxShortcutActivity::class.java),AppContext.getInstance().getString(R.string.game_folder))
    }
    fun installShortcut(componentName: ComponentName, shortcutName: String) {
        try {
            val addIntent = createShortcutIntent(componentName, shortcutName, false)
            addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
            AppContext.getInstance().sendBroadcast(addIntent)
        } catch (e: Exception) {

        }

    }

    fun uninstallShortcut(componentName: ComponentName, shortcutName: String) {
        try {
            val addIntent = createShortcutIntent(componentName, shortcutName, true)
            addIntent.action = "com.android.launcher.action.UNINSTALL_SHORTCUT"
            AppContext.getInstance().sendBroadcast(addIntent)
        } catch (e: Exception) {

        }

    }

    private fun createShortcutIntent(componentName: ComponentName, shortcutName: String, forDelete: Boolean): Intent {
        val shortcutIntent = Intent()
        shortcutIntent.component = componentName
        shortcutIntent.action = "com.couchgram.gamebooster.launch_gamebox"
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val addIntent = Intent()
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)
        addIntent.putExtra("duplicate", false)
        if (!forDelete) {
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(AppContext.getInstance(), R.mipmap.ic_shortcut))
        }
        return addIntent
    }

    fun hasInstallShortcut() = try{
        val CONTENT_URI = Uri.parse(getAuthorityFromPermission())
        LogUtils.v("DEBUG700","CONTENT_URI : $CONTENT_URI")
        val cursor = AppContext.getInstance().contentResolver.query(CONTENT_URI,arrayOf("title","iconResource"), "title=?",arrayOf("게임폴더",""), null)
        cursor?.use {
            it.count > 0
        }?:false
    }catch (e : Exception){
        false
    }

    fun getAuthorityFromPermission(): String {
        var authority: String? = getAuthorityFromPermissionDefault()
        if (authority == null || authority.trim { it <= ' ' } == "") {
            var packageName = getCurrentLauncherPackageName()
            packageName += ".permission.READ_SETTINGS"
            authority = getThirdAuthorityFromPermission(packageName)
        }
        if (TextUtils.isEmpty(authority)) {
            val sdkInt = android.os.Build.VERSION.SDK_INT
            val brand = android.os.Build.BRAND
            val isMi = brand?.let {
                ("miui".equals(brand,true) || "Xiaomi".equals(brand,true))
            }?:false
            LogUtils.v("DEBUG700","isMi : $isMi")
            if(isMi){
                authority = "com.miui.home.launcher.settings"
            }else{
                if (sdkInt < 8) {
                    authority = "com.android.launcher.settings"
                } else if (sdkInt < 19) {
                    authority = "com.android.launcher2.settings"
                } else {
                    authority = "com.android.launcher3.settings"
                }
            }
        }
        authority = "content://$authority/favorites?notify=true"
        return authority
    }

    fun getAuthorityFromPermissionDefault(): String {
        return getThirdAuthorityFromPermission("com.android.launcher.permission.READ_SETTINGS")
    }

    fun getThirdAuthorityFromPermission(permission: String): String {
        try {
            val packs = AppContext.getInstance().packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS) ?: return ""
            packs
                    .mapNotNull { it.providers }
                    .forEach {
                        it
                                .filter { permission == it.readPermission || permission == it.writePermission }
                                .map { it.authority }
                                .filter {
                                    !TextUtils.isEmpty(it) && (it
                                            .contains(".launcher.settings") || it
                                            .contains(".twlauncher.settings") || it
                                            .contains(".launcher2.settings"))
                                }
                                .forEach { return it }
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getCurrentLauncherPackageName(): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val res = AppContext.getInstance().packageManager.resolveActivity(intent, 0)
        return res?.let {
            if(it.activityInfo.packageName == "android") "" else it.activityInfo.packageName
        }?:""
    }
}