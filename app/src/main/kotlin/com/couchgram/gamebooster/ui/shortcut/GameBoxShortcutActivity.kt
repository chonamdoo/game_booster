package com.couchgram.gamebooster.ui.shortcut

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.R
import android.util.DisplayMetrics
import com.couchgram.gamebooster.ui.widget.pagerbottomtabstrip.TabItem
import com.couchgram.gamebooster.ui.widget.pagerbottomtabstrip.TabPagerAdapter
import com.couchgram.gamebooster.util.ActivityManager
import com.couchgram.gamebooster.util.TAG
import kotlinx.android.synthetic.main.activity_gamebox_shortcut.*
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem


/**
 * Created by chonamdoo on 2017. 5. 2..
 */


class GameBoxShortcutActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamebox_shortcut)
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        window.attributes.width = displayMetrics.widthPixels
        window.attributes.height = displayMetrics.heightPixels
        /*val navigationController = tab.custom()
                .addItem(newItem(R.drawable.ic_main_tap_boost_disabled,R.drawable.ic_main_tap_boost,"Boost"))
                .addItem(newItem(R.drawable.ic_main_tap_game_disabled,R.drawable.ic_main_tap_game,"Game")).build()
        val fragmentList = arrayListOf(GameBoxFragment.newInstance() as Fragment ,GameAdFragment.newInstance() as Fragment)
        viewPager.adapter = TabPagerAdapter(supportFragmentManager,fragmentList)
        navigationController.setupWithViewPager(viewPager)*/
        var gameBoxFragment : GameBoxFragment?= supportFragmentManager.findFragmentByTag(GameBoxFragment.TAG) as? GameBoxFragment
        if(gameBoxFragment == null){
            gameBoxFragment = GameBoxFragment.newInstance()
        }
        replaceFragmentToActivity(R.id.contentFrame,gameBoxFragment,GameBoxFragment.TAG)
        ActivityManager.finishAllActivity(this)
    }

    private fun newItem(drawable: Int, checkedDrawable: Int,title : String): BaseTabItem {
        val tabItem = TabItem(this)
        tabItem.initialize(drawable, checkedDrawable)
        tabItem.title = title
        tabItem.setTextDefaultColor(Color.parseColor("#979797"))
        tabItem.setTextCheckedColor(Color.parseColor("#00A2BF"))
        tabItem.setTabDefaultColor(Color.WHITE)
        tabItem.setTabCheckColor(Color.WHITE)
        return tabItem
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
    }
}