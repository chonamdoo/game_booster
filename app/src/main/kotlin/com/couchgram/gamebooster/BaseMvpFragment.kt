package com.couchgram.gamebooster

import android.os.Bundle
import android.view.View

/**
 * Created by chonamdoo on 2017. 5. 15..
 */

abstract class BaseMvpFragment<in VIEW : BaseView, P : BasePresenter<VIEW>> : BaseFragment(), BaseView {

    protected lateinit var presenter: P
        private set

    abstract fun onCreatePresenter(): P

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        presenter = onCreatePresenter()
        presenter.attachView(this as VIEW)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detachView()
    }

    override fun isNotFinish() = !activity.isFinishing
}