package com.couchgram.gamebooster.ui.boostapp.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import kotlinx.android.synthetic.main.boost_app_list_item.view.*
import android.view.MotionEvent
import android.widget.ImageView
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.couchgram.gamebooster.util.LogUtils

/**
 * Created by chonamdoo on 2017. 4. 29..
 */

class BoostAppAdapter(val context : Context,val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<BoostAppAdapter.ViewHolder>() , BoostAppAdapterContract.Model,BoostAppAdapterContract.View{
    val boostList = arrayListOf<BoostAppInfo>()
    val pm = AppContext.getInstance().packageManager!!
    var editMode = false
    var uninstallMode = false
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(boostList.size > position) {
            holder.onBind(context,boostList[position],editMode,uninstallMode,pm,position,onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.boost_app_list_item,parent,false))

    override fun getItemCount(): Int {
        return boostList.size
    }

    override fun itemSize(): Int {
        return itemCount
    }
    override fun addItems(items : ArrayList<BoostAppInfo>){
        boostList.addAll(items)
    }
    override fun clearItem(){
        boostList.clear()
    }
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun removeItems(boostAppInfo: BoostAppInfo,position : Int) {
        boostList.removeAt(position)
    }
    override fun getItem(position: Int): BoostAppInfo {
        return boostList[position]
    }

    override fun getItems(): ArrayList<BoostAppInfo> {
        return boostList
    }

    override fun updateEditMode(editMode: Boolean) {
        this.editMode = editMode
        uninstallMode = false
        notifyAdapter()
    }

    override fun updateUninstallMode(uninstallMode: Boolean) {
        this.uninstallMode = uninstallMode
        editMode = false
        notifyAdapter()
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun onBind(context: Context,boostAppInfo : BoostAppInfo,editMode : Boolean,uninstallMode : Boolean ,pm : PackageManager,position: Int,onItemClickListener: OnItemClickListener){
            itemView.boost_add_del.visibility = View.INVISIBLE
            if(boostAppInfo.isAddItem){
                if(uninstallMode || editMode){
                    itemView.add_boost_app.visibility = View.INVISIBLE
                    itemView.app_name.visibility = View.INVISIBLE
                    itemView.boost_app.visibility = View.INVISIBLE
                }else {
                    itemView.add_boost_app.visibility = View.VISIBLE
                    itemView.app_name.text = context.getString(R.string.add_game)
                    itemView.app_name.visibility = View.VISIBLE
                    itemView.boost_app.visibility = View.INVISIBLE
                }
            }else {
                itemView.add_boost_app.visibility = View.INVISIBLE
                itemView.app_name.visibility = View.VISIBLE
                itemView.app_name.text = boostAppInfo.appName
                itemView.boost_app.visibility = View.VISIBLE
                val gdh = GenericDraweeHierarchyBuilder(itemView.context.resources)
                        .setBackground(pm.getApplicationIcon(boostAppInfo.packageName))
                        .build()
                itemView.boost_app.hierarchy = gdh
                if((uninstallMode || editMode)){
                    itemView.boost_add_del.visibility = View.VISIBLE
                    itemView.boost_add_del.setBackgroundResource(if(uninstallMode) R.drawable.bg_uninstall_app else R.drawable.bg_boost_app_del)
                }else{
                }
            }
            itemView.setOnTouchListener { v, event ->
                if(!boostAppInfo.isAddItem){
                    val view = v.findViewById(R.id.boost_app) as ImageView
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if((uninstallMode || editMode)){
                                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.grey))
                            }
                            view.drawable.alpha = 50
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            if((uninstallMode || editMode)){
                                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.white))
                            }
                            view.drawable.alpha = 255
                        }
                    }
                }
                false
            }
            itemView.setOnClickListener {
                if((!editMode && !uninstallMode) || ((editMode || uninstallMode) && !boostAppInfo.isAddItem)){
                    onItemClickListener.onItemClick(boostAppInfo, position)
                }
            }
        }
    }
}