package com.couchgram.gamebooster.ui.shortcut

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.BaseFragment
import com.couchgram.gamebooster.BaseMvpFragment
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.api.AdsData
import com.couchgram.gamebooster.ui.shortcut.adapter.GameAdBoxAdapter
import com.couchgram.gamebooster.ui.shortcut.adapter.GameAdBoxHeaderAdapter
import com.couchgram.gamebooster.ui.shortcut.presenter.GameAdBoxContract
import com.couchgram.gamebooster.ui.shortcut.presenter.GameAdBoxPresenter
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.couchgram.gamebooster.ui.widget.view.LinearLayouItemDecoration
import com.couchgram.gamebooster.util.DisplayUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.rubengees.easyheaderfooteradapter.EasyHeaderFooterAdapter
import kotlinx.android.synthetic.main.fragment_gamebox_ad.*
import kotlinx.android.synthetic.main.offerwall_header.view.*

/**
 * Created by chonamdoo on 2017. 5. 2..
 */

class GameAdFragment : BaseMvpFragment<GameAdBoxContract.View,GameAdBoxContract.Presenter>() ,GameAdBoxContract.View{

    private lateinit var activity : GameBoxShortcutActivity
    private lateinit var boxAdapter: GameAdBoxAdapter
    private lateinit var headerAdapter : GameAdBoxHeaderAdapter
    private lateinit var headerFooterAdapter : EasyHeaderFooterAdapter

    private var headerView : View?= null

    override fun onCreatePresenter(): GameAdBoxContract.Presenter {
        return GameAdBoxPresenter(activity)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_gamebox_ad
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recyclerview.setHasFixedSize(true)
        recyclerview.addItemDecoration(LinearLayouItemDecoration(DisplayUtil.dpTopx(10F).toInt(),LinearLayoutManager.VERTICAL))
        boxAdapter = GameAdBoxAdapter(activity,onClickLisener)
        headerAdapter = GameAdBoxHeaderAdapter(activity)
        headerFooterAdapter = EasyHeaderFooterAdapter(boxAdapter)
        recyclerview.adapter = headerFooterAdapter
        presenter.run {
            adapterModel = boxAdapter
            adapterView = boxAdapter
            start()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as GameBoxShortcutActivity
    }

    companion object{
        fun newInstance() : GameAdFragment{
            return GameAdFragment()
        }
    }

    override fun getHeaderView(asData: AdsData?, list: List<AdsData>) {
        if(headerView == null){
            headerView = LayoutInflater.from(activity).inflate(R.layout.offerwall_header,rootlayout,false)
        }
        headerView?.let {
            headerView?.main_view?.visibility = asData?.let { View.VISIBLE }?: View.GONE

            /* headerView?.grid_view?.setHasFixedSize(true)
            headerView?.grid_view?.layoutManager = GridLayoutManager(activity,2)
            headerView?.grid_view?.boxAdapter = headerAdapter
            getHeaderAdapterItem(list)
            headerAdapter.addItems(headerAdapterItem)
            headerAdapter.notifyDataSetChanged()*/
            asData?.let {
                headerView?.tag = asData
                headerView?.setOnClickListener { v -> onClickLisener.onItemClick(v.tag as AdsData,-1) }
            }
            asData?.title?.let {
                headerView?.ad_top_title?.text = it
            }
            asData?.description?.let {
                headerView?.ad_top_desc?.text = it
            }
            asData?.cta_text?.let {
                headerView?.ad_top_btn_cta?.text = it
            }
            val imageRequest1 = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(asData?.banner_url))
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .build()
            val draweeController1 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest1)
                    .build()
            headerView?.ad_top_img?.controller = draweeController1
            val imageRequest2 = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(asData?.icon_url))
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .build()

            val draweeController2 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest2)
                    .build()
            headerView?.ad_top_img_icon?.controller = draweeController2
            if(headerView?.parent != null){
                headerFooterAdapter.removeHeader()
            }
            headerFooterAdapter.header = headerView
        }
    }

    internal val onClickLisener : OnItemClickListener = object : OnItemClickListener{
        override fun onItemClick(item: Any, position: Int) {
            if(item is AdsData){
                presenter.onClickTrackerEvnet(item)
            }
        }
    }
}