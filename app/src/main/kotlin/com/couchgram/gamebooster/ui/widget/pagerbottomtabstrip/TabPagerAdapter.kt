package com.couchgram.gamebooster.ui.widget.pagerbottomtabstrip

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup



/**
 * Created by chonamdoo on 2017. 5. 2..
 */

class TabPagerAdapter(fm : FragmentManager,val fragmentList : ArrayList<Fragment>) : FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position) as Fragment
    }
}