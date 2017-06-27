package com.couchgram.gamebooster.ui.boostapp.presenter

import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.BasePresenterImpl
import com.couchgram.gamebooster.BaseView
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.data.source.boostapp.BoostAppRepository
import com.couchgram.gamebooster.ui.boostapp.adapter.BoostAppAdapterContract
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.RxSchedulers
import com.couchgram.gamebooster.util.i
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by chonamdoo on 2017. 4. 30..
 */

class BoostAppPresenter(val activity: BaseActivity) : BasePresenterImpl<BoostAppContract.View>(), BoostAppContract.Presenter{

    lateinit override var adapterModel: BoostAppAdapterContract.Model
    lateinit override var adapterView: BoostAppAdapterContract.View
    private val boostAppRepository = BoostAppRepository.instance

    override fun start() {
        getBoostAppList()
    }
    override fun getBoostAppList(){
        compositeDisposable.add(boostAppRepository.getBoostAppList()
                .compose(RxSchedulers.io_main())
                .compose(RxSchedulers.applyProgress(activity))
                .bindUntilEvent(activity, ActivityEvent.DESTROY)
                .subscribeWith(object : DisposableSingleObserver<ArrayList<BoostAppInfo>>(){
                    override fun onError(e: Throwable?) {

                    }

                    override fun onSuccess(item: ArrayList<BoostAppInfo>) {
                        adapterModel.clearItem()
                        adapterModel.addItems(item)
                        adapterView.notifyAdapter()
                    }
                }))
    }
    override fun updateAddPosition() {
        Completable.fromAction {
            boostAppRepository.updatePositions(adapterModel.getItems().toMutableList())
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun removeBoostItem(boostAppInfo: BoostAppInfo,position : Int) {
        compositeDisposable.add(
                Completable.fromAction {
                    boostAppRepository.removeBoostApp(boostAppInfo.packageName)
                    adapterModel.removeItems(boostAppInfo,position)
                    if(adapterModel.itemSize() == 1){
                        adapterView.updateEditMode(false)
                    }
                    adapterView.notifyAdapter()
                }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        )
    }
}