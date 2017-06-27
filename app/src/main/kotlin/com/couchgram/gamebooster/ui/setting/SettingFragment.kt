package com.couchgram.gamebooster.ui.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.couchgram.gamebooster.BaseFragment
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.PERMS_GUIDE
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.ui.guide.CallWindowModeGuide
import com.couchgram.gamebooster.ui.guide.PermsGuide
import com.couchgram.gamebooster.ui.widget.dialog.ConfirmDialog
import com.couchgram.gamebooster.ui.widget.listener.SettingMenuClickListener
import com.couchgram.gamebooster.util.Utils
import ezy.assist.compat.SettingsCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_setting.*


/**
 * Created by chonamdoo on 2017. 5. 30..
 */

class SettingFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var activity: SettingActivity
    private val REQ_CALL_WINDOW_PERMS = 9880
    private val perms = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
    private var confirmDialog: ConfirmDialog? = null
    private var callWindowModeGuide: CallWindowModeGuide? = null
    private val permsGuide: PermsGuide by lazy {
        PermsGuide(activity, PERMS_GUIDE.NOTIFICATION_GUIDE)
    }
    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    override fun getLayoutResource(): Int {
        return R.layout.fragment_setting
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        incoming_call_window.onCheckChangeListener(settingMenuClickListener)
    }

    override fun onResume() {
        super.onResume()
        incoming_call_window.setChecked(Pref.call_window_mode)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as SettingActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        confirmDialog?.dismiss()
        callWindowModeGuide?.dismiss()
        permsGuide.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (perms.size == this.perms.size) {
            if (!SettingsCompat.canDrawOverlays(activity)) {
                checkDrawOverlayPerms(requestCode)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setRationale(getString(R.string.perms_req_use_feature)).setTitle(getString(R.string.req_perms)).build().show()
        }
    }

    fun checkDrawOverlayPerms(requestCode: Int) {
        confirmDialog?.dismiss()
        confirmDialog = ConfirmDialog(activity)
        confirmDialog?.setPositiveClickListener( View.OnClickListener {
            callWindowModeGuide = CallWindowModeGuide(activity, 1).apply {
                show()
            }
            SettingsCompat.manageDrawOverlays(activity)
            compositeDisposable.add(Utils.drawOverlayPermissionChecker()
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {}, {
                        Utils.reqOverlayHomeIntent(activity)
                        if (SettingsCompat.canDrawOverlays(activity)) {
                            when (requestCode) {
                                REQ_CALL_WINDOW_PERMS -> {
                                    Pref.call_window_mode = true
                                }
                            }
                        } else {
                            startActivity(Intent(activity, SettingActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
                            })
                        }
                    }))
        })
        confirmDialog?.setNegativeClickListener(View.OnClickListener {
            when (requestCode) {
                REQ_CALL_WINDOW_PERMS -> {
                    Pref.call_window_mode = false
                    incoming_call_window.setChecked(false)
                }
            }
        })
        confirmDialog?.setConfirmText(getString(R.string.setting))
        confirmDialog?.setTitleText(getString(R.string.noti))
        confirmDialog?.setContentText(if(requestCode == REQ_CALL_WINDOW_PERMS) getString(R.string.perms_call_widow_mode_desc) else getString(R.string.perms_disconnect_call))
        confirmDialog?.show()
    }

    private val settingMenuClickListener = object : SettingMenuClickListener {
        override fun menuCheckChange(view: View, checked: Boolean) {
            when (view.id) {
                R.id.incoming_call_window -> {
                    if (EasyPermissions.hasPermissions(context, *perms) && SettingsCompat.canDrawOverlays(activity) && Utils.isNotificationListenerSetting()) {
                        Pref.call_window_mode = checked
                        incoming_call_window.setChecked(checked)
                    } else {
                        if (!EasyPermissions.hasPermissions(context, *perms)) {
                            incoming_call_window.setChecked(false)
                            EasyPermissions.requestPermissions(this@SettingFragment,getString(R.string.perms_req_use_feature), REQ_CALL_WINDOW_PERMS, *perms)
                        } else if (!SettingsCompat.canDrawOverlays(activity)) {
                            checkDrawOverlayPerms(REQ_CALL_WINDOW_PERMS)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = SettingFragment()
    }

}