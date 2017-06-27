package com.couchgram.gamebooster.ui.installedapp.presenter

import com.couchgram.gamebooster.BasePresenter
import com.couchgram.gamebooster.BaseView
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.ui.installedapp.adapter.InstalledAppAdapterContract


/**
 * Created by chonamdoo on 2017. 4. 27..
 */

interface InstalledAppsContract{
    interface View : BaseView {
    }
    interface Presenter : BasePresenter<View> {
        fun getInstalledAppList()
        fun addEditInstalledApp(appInfo: AppInfo, position : Int)
        var adapterModel: InstalledAppAdapterContract.Model
        var adapterView: InstalledAppAdapterContract.View
    }

}