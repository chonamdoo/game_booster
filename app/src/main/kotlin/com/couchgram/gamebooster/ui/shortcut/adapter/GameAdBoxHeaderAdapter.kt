package com.couchgram.gamebooster.ui.shortcut.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.api.AdsData

/**
 * Created by chonamdoo on 2017. 5. 6..
 */

class GameAdBoxHeaderAdapter(val context: Context) : RecyclerView.Adapter<GameAdBoxHeaderAdapter.ViewHolder>(){
    val itemList = arrayListOf<AdsData>()
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun addItems(items : ArrayList<AdsData>){
        itemList.addAll(items)
    }
    fun clearItem(){
        itemList.clear()
    }
    fun getItems(position: Int) : AdsData?{
        if(itemList.size > position){
            return itemList[position]
        }
        return null
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
        = ViewHolder(LayoutInflater.from(context).inflate(R.layout.offerwall_header_grid_item,parent,false))

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun onBind(){

        }
    }
}