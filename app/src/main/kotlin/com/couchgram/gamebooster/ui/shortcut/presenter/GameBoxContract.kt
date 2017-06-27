package com.couchgram.gamebooster.ui.shortcut.presenter

import com.couchgram.gamebooster.BasePresenter
import com.couchgram.gamebooster.BaseView
import com.couchgram.gamebooster.ui.shortcut.adapter.GameBoxAdapterContract


/**
 * Created by chonamdoo on 2017. 5. 3..
 */
interface GameBoxContract {
    interface View : BaseView{

    }
    interface Presenter : BasePresenter<View>{
        fun getBoostAppList()
        fun updateAddPosition()
        var adapterModel : GameBoxAdapterContract.Model
        var adapterView : GameBoxAdapterContract.View
    }
}