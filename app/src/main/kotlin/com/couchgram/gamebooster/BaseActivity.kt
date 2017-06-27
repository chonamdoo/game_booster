package com.couchgram.gamebooster

import android.os.Bundle
import android.support.v4.app.Fragment
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.util.ActivityManager
import com.couchgram.gamebooster.util.DisplayUtil
import com.couchgram.gamebooster.util.OsVersions
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceObject
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_common.*

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

open class BaseActivity : RxAppCompatActivity(){
    var initialised = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!initialised && IronSourceObject.getInstance().ironSourceAppKey.isNullOrEmpty()){
            initialised = true
            Completable.fromAction {
//                IntegrationHelper.validateIntegration(this)
                IronSource.init(this,Constants.IRONSOURCE_APP_KEY,IronSource.AD_UNIT.REWARDED_VIDEO,IronSource.AD_UNIT.OFFERWALL)
            }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        }
        ActivityManager.addActivity(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setToolar()
    }
    fun replaceFragmentToActivity(mainViewId : Int, fragment: Fragment, tag : String){
        checkNotNull(fragment)
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(mainViewId,fragment,tag)
        transaction.commitAllowingStateLoss()
    }
    fun setToolar(){
        toolbar?.let {
            if(OsVersions.isAtLeastLollipop()){
                it.setPadding(0, DisplayUtil.getStatusBarHeight(),0,0)
            }
            setSupportActionBar(it)
        }
    }
    fun setArrowToolbar(){
        toolbar?.let {
            it.setNavigationIcon(R.drawable.btn_main_back)
            it.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }
}