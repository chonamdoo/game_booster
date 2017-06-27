package com.couchgram.gamebooster

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by chonamdoo on 2017. 5. 16..
 */

abstract class BasePresenterImpl<VIEW : BaseView> : BasePresenter<VIEW>{
    protected lateinit var view: VIEW
        private set

    private var isDetach = true

    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun attachView(view: VIEW) {
        this.view = view
        isDetach = false
    }

    override fun detachView() {
        isDetach = true
    }

    protected fun isDetach() = !isDetach and view.isNotFinish()

}