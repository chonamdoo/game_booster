package com.couchgram.gamebooster.ui.boostapp

import android.os.Bundle
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.util.ShortCutUtils
import com.couchgram.gamebooster.util.TAG

/**
 * Created by chonamdoo on 2017. 4. 28..
 */

class BoostAppListActivity : BaseActivity(){
    var boostAppListFragment : BoostAppListFragment?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        boostAppListFragment = supportFragmentManager.findFragmentByTag(BoostAppListFragment.TAG) as? BoostAppListFragment
        if(boostAppListFragment == null){
            boostAppListFragment = BoostAppListFragment.newInstance()
        }
        replaceFragmentToActivity(R.id.contentFrame,boostAppListFragment!!,BoostAppListFragment.TAG)
        if(!Pref.once_add_short_cut) {
            Pref.once_add_short_cut = true
            ShortCutUtils.createGameBoxActivityShortcut()
        }
    }

    override fun onBackPressed() {
        boostAppListFragment?.let {
            if(!it.isEditMode()){
                super.onBackPressed()
            }else{
                it.clearEditMode()
            }
        }?:super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}