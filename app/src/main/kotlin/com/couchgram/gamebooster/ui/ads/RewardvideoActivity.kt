package com.couchgram.gamebooster.ui.ads


import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import android.view.View
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.common.PERMS_GUIDE
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.service.BoostService
import com.couchgram.gamebooster.task.ProcessScan
import com.couchgram.gamebooster.ui.guide.PermsGuide
import com.couchgram.gamebooster.ui.widget.dialog.ConfirmDialog
import com.couchgram.gamebooster.util.*
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reward_video.*
import pub.devrel.easypermissions.EasyPermissions


/**
 * Created by chonamdoo on 2017. 5. 24..
 */

class RewardvideoActivity : BaseActivity() ,View.OnClickListener{

    private var boostAppInfo: BoostAppInfo? = null
    private var accessbilityDialog : ConfirmDialog?=null
    private val permsGuide: PermsGuide by lazy {
        PermsGuide(this, PERMS_GUIDE.ACCESSIBILITY_GUIDE)
    }
    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reward_video)

        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        window.attributes.width = displayMetrics.widthPixels
        window.attributes.height = displayMetrics.heightPixels

        boostAppInfo = intent?.extras?.get(Constants.BOOST_ITEM) as? BoostAppInfo
        initLayout()
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
       super.onRestoreInstanceState(savedInstanceState)
       savedInstanceState?.let {
           boostAppInfo = it.getParcelable(Constants.BOOST_ITEM)
       }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(Constants.BOOST_ITEM, boostAppInfo)
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it.id){
                R.id.ultra_mode ->{
                    if(!Utils.checkEnabledAccessibilityService()){
                        accessbilityDialog?.dismiss()
                        accessbilityDialog = ConfirmDialog(this).apply {
                            setPositiveClickListener(View.OnClickListener {
                                Utils.reqAccessibilityPerms(this@RewardvideoActivity)
                                permsGuide.show()
                                compositeDisposable.add(Utils.accessibilityPermissionChecker()
                                        .subscribeOn(Schedulers.trampoline())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({},{},{
                                            Utils.reqAccessibilityHomeIntent(this@RewardvideoActivity)
                                            startActivity(Intent(this@RewardvideoActivity, RewardvideoActivity::class.java).apply {
                                                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                            })
                                            if(Utils.checkEnabledAccessibilityService()){
                                                startUltraMode()
                                            }
                                        }))
                            })
                            setConfirmText(getString(R.string.setting))
                            setTitleText(getString(R.string.noti))
                            setContentText(getString(R.string.perms_boost))
                            show()
                        }
                    }else{
                        startUltraMode()
                    }
                }
                R.id.normal_mode ->{
                    startNormalMode()
                }
            }
        }
    }
    fun initLayout(){
        ultra_mode.setOnClickListener(this)
        normal_mode.setOnClickListener(this)
    }

    fun startNormalMode() {
        boostAppInfo?.let {
            startBoostAnim(Constants.NORMAL_MODE,it)
            Single.defer {
                val processScan = ProcessScan(this)
                val cleanMemSize = processScan.cleanTask(processScan.runningTaskList())
                Single.just(cleanMemSize)
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        cleanSize ->
                    }, {})
        }
    }

    fun startUltraMode() {
        stopService(Intent(this@RewardvideoActivity, BoostService::class.java))

        boostAppInfo?.let {
            startBoostAnim(Constants.ULTRA_MODE,it)
            Single.defer {
                val processScan = ProcessScan(this)
                val cleanMemSize = processScan.cleanTask(processScan.runningTaskList())
                Single.just(cleanMemSize)
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        cleanSize ->
                    }, {})
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun startBoostAnim(boostMode : Int, boostAppInfo: BoostAppInfo){
        Intent(this,BoostAnimActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Constants.BOOST_ITEM, boostAppInfo)
            putExtra(Constants.BOOST_MODE,boostMode)
            startActivity(this)
        }
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
    }
    override fun onDestroy() {
        super.onDestroy()
        LogUtils.v("DEBUG800","onDestroy")
        compositeDisposable.dispose()
        accessbilityDialog?.dismiss()
        permsGuide.dismiss()
    }
}