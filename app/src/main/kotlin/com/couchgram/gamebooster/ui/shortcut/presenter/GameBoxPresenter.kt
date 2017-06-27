package com.couchgram.gamebooster.ui.shortcut.presenter

import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.BasePresenterImpl
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.data.source.boostapp.BoostAppRepository
import com.couchgram.gamebooster.ui.shortcut.adapter.GameBoxAdapterContract
import com.couchgram.gamebooster.util.LogUtils
import com.couchgram.gamebooster.util.RxSchedulers
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by chonamdoo on 2017. 5. 3..
 */


class GameBoxPresenter(val acitvity : BaseActivity) : BasePresenterImpl<GameBoxContract.View>() , GameBoxContract.Presenter{
    lateinit override var adapterModel: GameBoxAdapterContract.Model
    lateinit override var adapterView: GameBoxAdapterContract.View
    private val boostAppRepository = BoostAppRepository.instance

    override fun start() {
        getBoostAppList()
    }
    override fun getBoostAppList(){
        compositeDisposable.add(boostAppRepository.getBoostAppList()
                .compose(RxSchedulers.io_main())
                .compose(RxSchedulers.applyProgress(acitvity))
                .bindUntilEvent(acitvity,ActivityEvent.DESTROY)
                .subscribeWith(object : DisposableSingleObserver<ArrayList<BoostAppInfo>>(){
                    override fun onError(e: Throwable?) {

                    }

                    override fun onSuccess(items: ArrayList<BoostAppInfo>) {
                        adapterModel.clearItem()
                        adapterModel.addItems(items)
                        adapterView.notifyAdapter()
                    }
                }))
    }

    override fun updateAddPosition() {
        Completable.fromAction {
            boostAppRepository.updatePositions(adapterModel.getItems().toMutableList())
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}