package com.couchgram.gamebooster.ui.installedapp

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.couchgram.gamebooster.BaseMvpFragment
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.ui.installedapp.adapter.InstalledAppAdapter
import com.couchgram.gamebooster.ui.installedapp.presenter.InstalledAppsContract
import com.couchgram.gamebooster.ui.installedapp.presenter.InstalledAppsPresenter
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_installed_app_list.*

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class InstalledAppListFragment : BaseMvpFragment<InstalledAppsContract.View,InstalledAppsContract.Presenter>(){

    private lateinit var installedAppAdapter : InstalledAppAdapter
    private lateinit var activity : InstalledAppListActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePresenter(): InstalledAppsContract.Presenter {
        return  InstalledAppsPresenter(activity)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_installed_app_list
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installedAppAdapter = InstalledAppAdapter(activity,onClickLitener)
        recyclerview.adapter = installedAppAdapter
        recyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recyclerview.setHasFixedSize(true)

        presenter.run {
            adapterModel = installedAppAdapter
            adapterView = installedAppAdapter
        }
        presenter.start()
        initAdBanner()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as InstalledAppListActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adsView?.destroy()
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    companion object{
        fun newInstance() = InstalledAppListFragment()
    }

    internal var onClickLitener : OnItemClickListener = object : OnItemClickListener{
        override fun onItemClick(item: Any, position: Int) {
            if(item is AppInfo) {
                presenter.addEditInstalledApp(item, position)
            }
        }
    }

    fun initAdBanner(){
        adsView.adUnitId = Constants.MOPUB_AD_ID
        adsView.bannerAdListener = object : MoPubView.BannerAdListener{
            override fun onBannerLoaded(banner: MoPubView) {
            }

            override fun onBannerFailed(banner: MoPubView, errorCode: MoPubErrorCode) {
            }

            override fun onBannerClicked(banner: MoPubView) {

            }

            override fun onBannerExpanded(banner: MoPubView) {

            }

            override fun onBannerCollapsed(banner: MoPubView) {

            }
        }
        adsView.loadAd()
    }
}