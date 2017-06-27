package com.couchgram.gamebooster

import android.os.Bundle

/**
 * Created by chonamdoo on 2017. 5. 16..
 */

abstract class BaseMvpActivity<in VIEW: BaseView, P : BasePresenter<VIEW>> : BaseActivity(), BaseView {

    protected lateinit var presenter: P
        private set

    abstract fun onCreatePresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = onCreatePresenter()
        presenter.attachView(this as VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detachView()
    }

    override fun isNotFinish() = !isFinishing
}