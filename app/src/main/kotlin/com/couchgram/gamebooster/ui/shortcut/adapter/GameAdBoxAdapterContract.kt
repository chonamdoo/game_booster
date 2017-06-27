package com.couchgram.gamebooster.ui.shortcut.adapter

import com.couchgram.gamebooster.api.AdsData

/**
 * Created by chonamdoo on 2017. 5. 7..
 */

interface GameAdBoxAdapterContract{
    interface View{
        fun notifyAdapter()
    }
    interface Model{

        fun addItems(list : List<AdsData>)
        fun clearItem()
    }

}