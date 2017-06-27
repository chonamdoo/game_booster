package com.couchgram.gamebooster.ui.shortcut.presenter

import com.couchgram.gamebooster.BasePresenter
import com.couchgram.gamebooster.BaseView
import com.couchgram.gamebooster.api.AdsData
import com.couchgram.gamebooster.ui.shortcut.adapter.GameAdBoxAdapterContract

/**
 * Created by chonamdoo on 2017. 5. 7..
 */

interface GameAdBoxContract {
    interface View : BaseView{
        fun getHeaderView(asData : AdsData?,list : List<AdsData>)
    }
    interface Presenter : BasePresenter<View>{
        fun onClickTrackerEvnet(adsData: AdsData)
        fun getAdList()
        var adapterModel : GameAdBoxAdapterContract.Model
        var adapterView : GameAdBoxAdapterContract.View
    }
}
