package com.couchgram.gamebooster.ui.installedapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.data.source.AppInfo
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import kotlinx.android.synthetic.main.installed_app_list_item.view.*


/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class InstalledAppAdapter(val context : Context ,val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<InstalledAppAdapter.ViewHolder>() , InstalledAppAdapterContract.View, InstalledAppAdapterContract.Model {

    private val installedAppList = arrayListOf<AppInfo>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(installedAppList[position],position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.installed_app_list_item,parent,false),onItemClickListener)

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(appInfos: ArrayList<AppInfo>) {
        installedAppList.addAll(appInfos)
    }

    override fun clearItem() {
        installedAppList.clear()
    }

    override fun getItem(position: Int): AppInfo {
        return installedAppList[position]
    }

    override fun getItems(): ArrayList<AppInfo> {
        return installedAppList
    }
    override fun getItemCount(): Int {
        return installedAppList.size
    }

    class ViewHolder(view : View, val onItemClickListener : OnItemClickListener) : RecyclerView.ViewHolder(view){
        fun onBind(appInfo: AppInfo, position : Int){
            val gdh = GenericDraweeHierarchyBuilder(itemView.context.resources)
                    .setBackground(appInfo.appIcon)
                    .build()
            itemView.tag = position
            itemView.app_name.text = appInfo.appName
            itemView.app_icon.hierarchy = gdh
            itemView.add_boost.isChecked = appInfo.isChecked
            if(appInfo.isChecked){
                itemView.add_boost.setBackgroundResource(R.drawable.btn_game_plus_completion)
            }else{
                itemView.add_boost.setBackgroundResource(R.drawable.bg_game_add_btn)
            }
            itemView.row_layout.setOnClickListener { v ->
                val toggleBtn = v.findViewById(R.id.add_boost) as ToggleButton
                val pos = v.tag as Int
                toggleBtn.isChecked = !toggleBtn.isChecked
                appInfo.isChecked = toggleBtn.isChecked
                if(toggleBtn.isChecked){
                    toggleBtn.setBackgroundResource(R.drawable.btn_game_plus_completion)
                }else{
                    toggleBtn.setBackgroundResource(R.drawable.bg_game_add_btn)
                }
                onItemClickListener.onItemClick(appInfo,pos)
            }
        }
    }
}