package com.couchgram.gamebooster.ui.boostapp.adapter

import com.couchgram.gamebooster.data.source.BoostAppInfo

/**
 * Created by chonamdoo on 2017. 4. 30..
 */
interface BoostAppAdapterContract {
    interface View{
        fun notifyAdapter()
        fun updateEditMode(editMode : Boolean)
        fun updateUninstallMode(uninstallMode : Boolean)
    }
    interface Model{
        fun removeItems(boostAppInfo: BoostAppInfo,position : Int)
        fun addItems(items : ArrayList<BoostAppInfo>)
        fun getItem(position : Int) : BoostAppInfo
        fun getItems() : ArrayList<BoostAppInfo>
        fun clearItem()
        fun itemSize() : Int
    }
}