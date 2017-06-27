package com.couchgram.gamebooster.ui.shortcut

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.couchgram.gamebooster.BaseMvpFragment
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.ui.ads.RewardvideoActivity
import com.couchgram.gamebooster.ui.installedapp.InstalledAppListActivity
import com.couchgram.gamebooster.ui.shortcut.adapter.GameBoxAdapter
import com.couchgram.gamebooster.ui.shortcut.presenter.GameBoxContract
import com.couchgram.gamebooster.ui.shortcut.presenter.GameBoxPresenter
import com.couchgram.gamebooster.ui.widget.helper.ItemTouchHelperCallback
import com.couchgram.gamebooster.ui.widget.helper.OnStartDragListener
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.couchgram.gamebooster.ui.widget.view.GridAutofitLayoutManager
import com.couchgram.gamebooster.util.*
import com.ironsource.mediationsdk.IronSource
import com.mobvista.msdk.MobVistaConstans
import com.mobvista.msdk.out.MobVistaSDKFactory
import kotlinx.android.synthetic.main.fragment_gamebox.*
import java.util.*

/**
 * Created by chonamdoo on 2017. 5. 2..
 */
class GameBoxFragment : BaseMvpFragment<GameBoxContract.View,GameBoxContract.Presenter>() , OnStartDragListener ,View.OnClickListener{

    private lateinit var adapter: GameBoxAdapter
    private lateinit var activity: GameBoxShortcutActivity
    private lateinit var touchHelper: ItemTouchHelper

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as GameBoxShortcutActivity
    }
    override fun onCreatePresenter(): GameBoxContract.Presenter {
        return GameBoxPresenter(activity)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_gamebox
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.layoutManager = GridAutofitLayoutManager(context,context.resources.getDimension(R.dimen.boost_layout_width ).toInt())
        recyclerview.setHasFixedSize(true)
        adapter = GameBoxAdapter(activity,recyclerview,this,onClickLitener)
        recyclerview.adapter = adapter
        val itemTouchCallback = ItemTouchHelperCallback(adapter)
        touchHelper = ItemTouchHelper(itemTouchCallback)
        touchHelper.attachToRecyclerView(recyclerview)
        presenter.run {
            adapterModel = adapter
            adapterView = adapter
        }

//        val cal = Calendar.getInstance()
//        cal.time = Utils.currentDate()
//        title_hot_game.text =  getString(R.string.hot_game,cal.get(Calendar.YEAR).toString())
        txt_game_optimized_percent.text = getString(R.string.use_memory,"${RamUtils.getUseMemPercent()}%")
        hot_game_layout.setOnClickListener(this)
        img_offerwall.setOnClickListener(this)
        preloadWall()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }
    var onClickLitener : OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(item: Any, position: Int) {
            if(item is BoostAppInfo) {
                if(item.isAddItem){
                    activity.startActivity(Intent(activity, InstalledAppListActivity::class.java))
                }else {
                    Intent(activity, RewardvideoActivity::class.java).apply {
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra(Constants.BOOST_ITEM, item)
                        startActivity(this)
                    }
                }
            }
        }
    }
    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    override fun onEndDrag() {
        presenter.updateAddPosition()
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it.id){
                R.id.hot_game_layout -> {
                    loadAppWall()
                }
                R.id.img_offerwall ->{
                    if(IronSource.isOfferwallAvailable()){
                        IronSource.showOfferwall("shortcut_boost")
                    }
                }
            }
        }
    }

    fun preloadWall() {
        val sdk = MobVistaSDKFactory.getMobVistaSDK()
        val preloadMap = HashMap<String, Any>()
        preloadMap.put(MobVistaConstans.PROPERTIES_LAYOUT_TYPE, MobVistaConstans.LAYOUT_APPWALL)
        preloadMap.put(MobVistaConstans.PROPERTIES_UNIT_ID, "13321")
        sdk.preload(preloadMap)
    }
    fun loadAppWall(){
        try {
            Intent(activity, Class.forName("com.mobvista.msdk.shell.MVActivity")).apply {
                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                putExtra(MobVistaConstans.PROPERTIES_UNIT_ID, "13321")
                putExtra(MobVistaConstans.PROPERTIES_WALL_STATUS_COLOR, R.color.blue)
                putExtra(MobVistaConstans.PROPERTIES_WALL_TITLE_BACKGROUND_ID, R.color.blue)
                putExtra(MobVistaConstans.PROPERTIES_WALL_TAB_INDICATE_LINE_BACKGROUND_ID, R.color.blue)
                startActivity(this)
            }

        } catch (e: Exception) {
        }
    }
    companion object{
        fun newInstance() = GameBoxFragment()
    }
}
