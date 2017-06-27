package com.couchgram.gamebooster.ui.installedapp.presenter

import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.BasePresenterImpl
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.data.source.boostapp.BoostAppRepository
import com.couchgram.gamebooster.ui.installedapp.adapter.InstalledAppAdapterContract
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.RxSchedulers
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class InstalledAppsPresenter(val activity: BaseActivity) : BasePresenterImpl<InstalledAppsContract.View>() , InstalledAppsContract.Presenter{
    private val boostAppRepository = BoostAppRepository.instance
    lateinit override var adapterModel : InstalledAppAdapterContract.Model
    lateinit override var adapterView : InstalledAppAdapterContract.View

    override fun getInstalledAppList(){
        compositeDisposable.add(boostAppRepository.getInstalledAppList()
                .compose(RxSchedulers.io_main())
                .compose(RxSchedulers.applyProgress(activity))
                .bindUntilEvent(activity, ActivityEvent.DESTROY)
                .subscribeWith(object : DisposableSingleObserver<ArrayList<AppInfo>>(){
                    override fun onError(e: Throwable?) {

                    }

                    override fun onSuccess(items: ArrayList<AppInfo>) {
                        adapterModel.clearItem()
                        adapterModel.addItems(items)
                        adapterView.notifyAdapter()
                    }
                }))
    }

    override fun start() {
        getInstalledAppList()
    }
    override fun addEditInstalledApp(appInfo: AppInfo, position: Int) {
        if(!boostAppRepository.hasBoostApp(appInfo.packageName)) {
            boostAppRepository.addBoostApp(appInfo)
        }else{
            boostAppRepository.removeBoostApp(appInfo.packageName)
        }
        adapterModel.getItems()[position] = appInfo

    }

}