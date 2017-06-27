package com.couchgram.gamebooster.ui.shortcut.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.api.AdsData
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.offerwall_list_item.view.*

/**
 * Created by chonamdoo on 2017. 5. 6..
 */

class GameAdBoxAdapter(val context: Context, val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<GameAdBoxAdapter.ViewHolder>() , GameAdBoxAdapterContract.Model, GameAdBoxAdapterContract.View{

    val itemList = arrayListOf<AdsData>()
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.offerwall_list_item,parent,false),onItemClickListener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemList.size > position) {
            holder.onBind(getItems(position), position)
        }
    }
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(list: List<AdsData>) {
        itemList.addAll(list)
    }
    override fun clearItem(){
        itemList.clear()
    }
    fun getItems(position: Int) : AdsData{
        return itemList[position]
    }
    class ViewHolder(view : View,val onItemClickListener : OnItemClickListener) : RecyclerView.ViewHolder(view){
        fun onBind(adsData: AdsData,position: Int){
            val imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(adsData.icon_url))
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .build()

            val draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .build()
            itemView.ad_img.controller = draweeController
            adsData.cta_text?.let { itemView.btn_cta.text = it }
            adsData.title?.let { itemView.ad_title.text = it }
            adsData.description?.let { itemView.ad_desc.text = it }
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(adsData,position)
            }
        }
    }
}