package com.couchgram.gamebooster.ui.shortcut.presenter

import android.content.Intent
import android.net.Uri
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.BaseActivity
import com.couchgram.gamebooster.BasePresenterImpl
import com.couchgram.gamebooster.api.Api
import com.couchgram.gamebooster.api.AdsData
import com.couchgram.gamebooster.api.TrackerData
import com.couchgram.gamebooster.data.preference.Pref
import com.couchgram.gamebooster.ui.shortcut.adapter.GameAdBoxAdapterContract
import com.couchgram.gamebooster.util.RxSchedulers
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Created by chonamdoo on 2017. 5. 7..
 */
class GameAdBoxPresenter(val activity: BaseActivity) : BasePresenterImpl<GameAdBoxContract.View>() , GameAdBoxContract.Presenter {

    lateinit override var adapterModel : GameAdBoxAdapterContract.Model
    lateinit override var adapterView: GameAdBoxAdapterContract.View
    override fun getAdList() {
        compositeDisposable.add(Api.adsReqService.reqGetAdsList(getOfferWallParam())
                .compose(RxSchedulers.io_main())
                .compose(RxSchedulers.applyProgress(activity))
                .bindUntilEvent(activity, ActivityEvent.DESTROY)
                .subscribeWith(object : DisposableSingleObserver<ArrayList<AdsData>>(){
                    override fun onSuccess(items: ArrayList<AdsData>?) {
                        items?.let {
                            if(it.isNotEmpty()){
                                val list = it.toMutableList()
                                val asData = getHeaderData(list)
                                view.getHeaderView(asData,list)
                                adapterModel.clearItem()
                                adapterModel.addItems(list.toList())
                            }
                            adapterView.notifyAdapter()
                        }
                    }

                    override fun onError(e: Throwable?) {
                    }
                }))
    }
    private fun getHeaderData(list: MutableList<AdsData>): AdsData? {
        list.forEach {
            if (it.horizontal.equals("y")){
                list.remove(it)
                return it
            }
        }
        return null
        /*return list.indices
                .map { list[it] }
                .firstOrNull {
                    it.horizontal.equals("y")
                }*/
    }

    private fun getOfferWallParam(): Map<String, String> {
        val param = HashMap<String, String>()
        param.put("zone", "offerwall")
        param.put("adid", Pref.advertiseId)
        return param
    }

    override fun start() {
        getAdList()
    }
    override fun onClickTrackerEvnet(adsData: AdsData) {
        adsData.real_click_log?.let {
            Api.adsReqService.reqTracker(it.substring(it.indexOf("="),it.length))
                    .subscribeOn(Schedulers.io())
                    .retry(3)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<TrackerData> {
                        override fun onSubscribe(@NonNull d: Disposable) {

                        }

                        override fun onSuccess(@NonNull trackerData: TrackerData) {

                        }

                        override fun onError(@NonNull e: Throwable) {

                        }
                    })
            adsData.real_click_url?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                AppContext.getInstance().startActivity(intent)
            }
            /*try {
                LogUtils.v("DEBUG600","adClick : $url")
                val httpClient = OkHttpClient()
                val request = Request.Builder().url(url).build()
                httpClient.interceptors().add(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        // try the request
                        var response: Response? = doRequest(chain, request)
                        var tryCount = 0
                        val RetryCount = 3
                        while (response == null && tryCount <= RetryCount) {
                            val url = request.url().toString()
                            val newRequest = request.newBuilder().url(url).build()
                            tryCount++
                            response = doRequest(chain, newRequest)
                        }
                        if (response == null) {
                            throw IOException()
                        }
                        return response
                    }

                    private fun doRequest(chain: Interceptor.Chain, request: Request): Response? {
                        var response: Response? = null
                        try {
                            response = chain.proceed(request)
                        } catch (e: Exception) {
                        }

                        return response
                    }
                })
                val response = httpClient.newCall(request).execute()
                LogUtils.v("DEBUG700","response : ${response.isSuccessful}")
                response.body().close()
            }catch (e : Exception){
                e.printStackTrace()
                LogUtils.v("DEBUG700","response e : ${e.message}")
            }*/
        }
    }
}