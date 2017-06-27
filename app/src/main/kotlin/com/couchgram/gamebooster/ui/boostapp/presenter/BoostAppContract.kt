package com.couchgram.gamebooster.ui.boostapp.presenter

import com.couchgram.gamebooster.BasePresenter
import com.couchgram.gamebooster.BaseView
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.ui.boostapp.adapter.BoostAppAdapterContract

/**
 * Created by chonamdoo on 2017. 4. 30..
 */
interface BoostAppContract {
    interface View : BaseView {
        fun isEditMode() : Boolean
        fun clearEditMode()
    }
    interface Presenter : BasePresenter<View> {
        fun removeBoostItem(boostAppInfo: BoostAppInfo,position : Int)
        fun getBoostAppList()
        fun updateAddPosition()
        var adapterModel : BoostAppAdapterContract.Model
        var adapterView : BoostAppAdapterContract.View
    }
}