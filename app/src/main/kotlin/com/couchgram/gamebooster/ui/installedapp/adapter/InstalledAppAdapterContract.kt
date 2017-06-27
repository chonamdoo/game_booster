package com.couchgram.gamebooster.ui.installedapp.adapter

import com.couchgram.gamebooster.data.source.AppInfo

/**
 * Created by chonamdoo on 2017. 4. 28..
 */

interface InstalledAppAdapterContract{
    interface View{
        fun notifyAdapter()
    }
    interface Model{
        fun addItems(appInfos : ArrayList<AppInfo>)
        fun clearItem()
        fun getItem(position : Int) : AppInfo
        fun getItems() : ArrayList<AppInfo>
    }

}