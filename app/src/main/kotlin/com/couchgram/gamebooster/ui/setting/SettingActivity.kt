package com.couchgram.gamebooster.ui.setting

import android.os.Bundle
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.TAG
import kotlinx.android.synthetic.main.activity_common.*

/**
 * Created by chonamdoo on 2017. 5. 30..
 */

class SettingActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        setArrowToolbar()
        replaceFragmentToActivity(R.id.contentFrame,SettingFragment.newInstance(),SettingFragment.TAG)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}