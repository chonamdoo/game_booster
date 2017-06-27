package com.couchgram.gamebooster

/**
 * Created by chonamdoo on 2017. 5. 15..
 */

interface BasePresenter<in VIEW : BaseView>{
    fun attachView(view: VIEW)
    fun detachView()
    fun start()
}