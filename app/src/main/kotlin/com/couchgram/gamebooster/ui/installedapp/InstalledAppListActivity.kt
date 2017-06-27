package com.couchgram.gamebooster.ui.installedapp

import android.os.Bundle
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.util.TAG
import com.ironsource.mediationsdk.IronSource

/**
 * Created by chonamdoo on 2017. 4. 27..
 */
class InstalledAppListActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        setArrowToolbar()
        var installAppFragment : InstalledAppListFragment?= supportFragmentManager.findFragmentByTag(InstalledAppListFragment.TAG) as? InstalledAppListFragment
        if(installAppFragment == null){
            installAppFragment = InstalledAppListFragment.newInstance()
        }
        replaceFragmentToActivity(R.id.contentFrame,installAppFragment,InstalledAppListFragment.TAG)
    }
}