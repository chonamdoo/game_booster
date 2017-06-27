package com.couchgram.gamebooster.ui.shortcut.adapter

import com.couchgram.gamebooster.data.source.BoostAppInfo

/**
 * Created by chonamdoo on 2017. 5. 7..
 */
interface GameBoxAdapterContract {
    interface View{
        fun notifyAdapter()
    }
    interface Model{
        fun addItems(items : ArrayList<BoostAppInfo>)
        fun getItem(position : Int) : BoostAppInfo
        fun getItems() : ArrayList<BoostAppInfo>
        fun clearItem()
    }
}