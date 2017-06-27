package com.couchgram.gamebooster.util

import android.app.Activity
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 * Created by chonamdoo on 2017. 4. 30..
 */
object RxSchedulers {
    fun <T> loading() = LoadingViewTransformer<T>()

    /*fun <T> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }*/

    /*fun <T> applyProgress(activity : Activity): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.doOnSubscribe {
                DialogUtils.showDialog(activity)
            }
                    *//*.doOnComplete {
                        Log.v("DEBUG700", "doOnComplete")
                    }
                    .doOnTerminate {
                        DialogUtils.dismissDialog()
                        Log.v("DEBUG700", "doOnTerminate")
                    }*//*
                    .doOnDispose({
                        LogUtils.v("DEBUG700", "doOnDispose")
                    })
                    .doFinally {
                        DialogUtils.dismissDialog()
                        LogUtils.v("DEBUG700", "doFinally")
                    }

        }
    }*/
    fun<T> io_main(): SingleTransformer<T,T>{
        return SingleTransformer {
            upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
    fun <T> applyProgress(activity : Activity): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.doOnSubscribe {
                DialogUtils.showDialog(activity)
            }
                    /*.doOnComplete {
                        Log.v("DEBUG700", "doOnComplete")
                    }
                    .doOnTerminate {
                        DialogUtils.dismissDialog()
                        Log.v("DEBUG700", "doOnTerminate")
                    }*/
                    .doOnDispose({
                        LogUtils.v("DEBUG700", "doOnDispose")
                    })
                    .doFinally {
                        DialogUtils.dismissDialog()
                        LogUtils.v("DEBUG700", "doFinally")
                    }

        }
    }
    class LoadingViewTransformer<T>() : ObservableTransformer<T, T>, SingleTransformer<T, T>, CompletableTransformer {
        override fun apply(upstream: Completable): CompletableSource {
            return upstream
                    .doOnSubscribe { /*loadingView.showLoadingIndicator()*/ }
                    .doOnTerminate { /*loadingView.hideLoadingIndicator() */}
        }

        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream
                    .doOnSubscribe { /*loadingView.showLoadingIndicator()*/ }
                    .doOnDispose { /*loadingView.hideLoadingIndicator()*/ }
                    .doOnError { /*loadingView.hideLoadingIndicator()*/ }
                    .doOnSuccess { /*loadingView.hideLoadingIndicator()*/ }
        }

        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream
                    .doOnSubscribe { /*loadingView.showLoadingIndicator()*/ }
                    .doOnDispose { /*loadingView.hideLoadingIndicator()*/ }
                    .doOnTerminate { /*loadingView.hideLoadingIndicator()*/ }
        }
    }
}