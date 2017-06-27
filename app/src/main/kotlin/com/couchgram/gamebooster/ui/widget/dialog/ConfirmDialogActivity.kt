package com.couchgram.gamebooster.ui.widget.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.service.BoostService

/**
 * Created by chonamdoo on 2017. 5. 26..
 */

class ConfirmDialogActivity : BaseActivity(){
    lateinit var corfirmDialog : ConfirmDialog
    lateinit var boostAppInfo : BoostAppInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boostAppInfo = intent?.extras?.get(Constants.BOOST_ITEM) as BoostAppInfo
        corfirmDialog = ConfirmDialog(this)
        corfirmDialog.setTitleText(getString(R.string.app_name))
        corfirmDialog.setContentText(getString(R.string.end_boost,boostAppInfo.appName))
        corfirmDialog.setCancleText(getString(R.string.close))
        corfirmDialog.setConfirmText(getString(R.string.finish))
        corfirmDialog.setDialogClickListener(View.OnClickListener {
            BoostService.isShowDestroyBoosterPopUp = true
            finish()
        },View.OnClickListener {
            stopService(Intent(this,BoostService::class.java))
            finish()
        })
        corfirmDialog.show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {

    }
}