package com.couchgram.gamebooster.ui.ads

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.service.BoostService
import com.couchgram.gamebooster.util.ActivityManager
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.Utils
import kotlinx.android.synthetic.main.activity_boost_anim.*

/**
 * Created by chonamdoo on 2017. 6. 7..
 */


class BoostAnimActivity : BaseActivity(){
    var boostMode = 1
    var boostItem : BoostAppInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boost_anim)

        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        window.attributes.width = displayMetrics.widthPixels
        window.attributes.height = displayMetrics.heightPixels

        boostItem = intent?.extras?.get(Constants.BOOST_ITEM) as? BoostAppInfo
        boostMode = intent?.extras?.getInt(Constants.BOOST_MODE,1) as Int
        boostItem?.let {
            boost_anim.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    lunchApp(boostMode,it)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    finish()
                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            boost_anim.playAnimation()
        }?:finish()
    }

    private fun lunchApp(mode : Int , boostAppInfo : BoostAppInfo){
        Utils.lunchApp(boostAppInfo.packageName)
        when(mode){
            Constants.ULTRA_MODE ->{
                startService(Intent(this, BoostService::class.java).apply { putExtra("boost_item", boostAppInfo) })
            }
            Constants.NORMAL_MODE ->{
                stopService(Intent(this, BoostService::class.java))
            }
        }
        finish()
        ActivityManager.finishAllActivity()
    }

    override fun onBackPressed() {

    }
}