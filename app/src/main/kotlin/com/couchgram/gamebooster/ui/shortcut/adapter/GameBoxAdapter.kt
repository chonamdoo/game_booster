package com.couchgram.gamebooster.ui.shortcut.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.couchgram.gamebooster.AppContext
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.ui.widget.helper.ItemTouchHelperAdapter
import com.couchgram.gamebooster.ui.widget.helper.ItemTouchHelperViewHolder
import com.couchgram.gamebooster.ui.widget.helper.OnStartDragListener
import com.couchgram.gamebooster.ui.widget.helper.RecyclerViewPositionHelper
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.couchgram.gamebooster.util.DisplayUtil
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import kotlinx.android.synthetic.main.boost_app_list_item.view.*
import java.util.*

/**
 * Created by chonamdoo on 2017. 5. 3..
 */

class GameBoxAdapter(val context: Context,val recyclerView: RecyclerView,val onStartDragListener: OnStartDragListener,val onClickLitener : OnItemClickListener) : RecyclerView.Adapter<GameBoxAdapter.ViewHolder>() ,
        GameBoxAdapterContract.Model, GameBoxAdapterContract.View, ItemTouchHelperAdapter{

    val boostList = arrayListOf<BoostAppInfo>()
    val pm = context.packageManager!!
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(boostList.size > position) {
            holder.onBind(context,boostList[position], position,pm,onStartDragListener,onClickLitener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.boost_app_list_item,parent,false),recyclerView,this@GameBoxAdapter)

    override fun getItemCount(): Int {
        return boostList.size
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

    override fun getItem(position: Int): BoostAppInfo {
        return boostList[position]
    }

    override fun getItems(): ArrayList<BoostAppInfo> {
        return boostList
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if(toPosition == (itemCount -1))
            return false
        swapItems(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        onStartDragListener.onEndDrag()
        return true
    }
    fun swapItems(fromPosition: Int, toPosition: Int) = if (fromPosition < toPosition) {
        for (i in fromPosition..toPosition - 1) {
            Collections.swap(boostList, i, i + 1)
        }
    } else {
        for (i in fromPosition downTo toPosition + 1) {
            Collections.swap(boostList, i, i - 1)
        }
    }
    override fun onItemDismiss(position: Int) {
        boostList.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onItemDrag(position: Int): Boolean {
        return position != (itemCount - 1)
    }
    class ViewHolder(view : View,val recyclerView: RecyclerView ,val gameBoxAdapter: GameBoxAdapter) : RecyclerView.ViewHolder(view) , ItemTouchHelperViewHolder {

        val recyclerViewPosHelper = RecyclerViewPositionHelper(recyclerView)
        fun onBind(context: Context, boostAppInfo : BoostAppInfo ,position: Int,pm : PackageManager,onStartDragListener: OnStartDragListener,onClickLitener : OnItemClickListener){
            itemView.tag = position
            if(boostAppInfo.isAddItem){
                itemView.add_boost_app.visibility = View.VISIBLE
                itemView.app_name.text = context.getString(R.string.add_game)
                itemView.boost_app.visibility = View.INVISIBLE
            }else {
                itemView.add_boost_app.visibility = View.INVISIBLE
                itemView.app_name.text = boostAppInfo.appName
                itemView.boost_app.visibility = View.VISIBLE
                val gdh = GenericDraweeHierarchyBuilder(itemView.context.resources)
                        .setBackground(pm.getApplicationIcon(boostAppInfo.packageName))
                        .build()
                itemView.boost_app.hierarchy = gdh
            }
            itemView.setOnClickListener {
                onClickLitener.onItemClick(boostAppInfo,position)
            }
            itemView.setOnTouchListener { v, event ->
                val view = v.findViewById(R.id.boost_app) as ImageView
                if(!boostAppInfo.isAddItem){
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.drawable.alpha = 50
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            view.drawable.alpha = 255
                        }
                    }
                }
                false
            }
            itemView.setOnLongClickListener {
                if(!boostAppInfo.isAddItem){
                    onStartDragListener.onStartDrag(this@ViewHolder)
                }
                false
            }
        }
        var elevationAnimator: ObjectAnimator? = null
        override fun onItemSelected() {
            if(itemView.tag as Int != gameBoxAdapter.itemCount-1){
                itemView.app_name.visibility = View.GONE
                itemView.boost_app.scaleX = 1.2f
                itemView.boost_app.scaleY = 1.2f

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    elevationAnimator?.cancel()
                    val toValue: Float =  DisplayUtil.getPixelFromDp(AppContext.getInstance(),5f).toFloat()
                    elevationAnimator = ObjectAnimator.ofFloat(itemView, "translationZ", itemView.translationZ, toValue)
                    elevationAnimator?.duration = 100
                    elevationAnimator?.start()
                }
                val visibleLastItemPos = recyclerViewPosHelper.findLastVisibleItemPosition()

                if(recyclerViewPosHelper.findLastVisibleItemPosition() == (gameBoxAdapter.itemCount-1)){
                    (recyclerView.findViewHolderForLayoutPosition(visibleLastItemPos) as GameBoxAdapter.ViewHolder).apply {
                        itemView.add_boost_app.visibility = View.INVISIBLE
                        itemView.app_name.visibility = View.INVISIBLE
                    }
                }
            }
        }

        override fun onItemClear() {
            itemView.app_name.visibility = View.VISIBLE
            itemView.boost_app.scaleX = 1.0f
            itemView.boost_app.scaleY = 1.0f

            val visibleLastItemPos = recyclerViewPosHelper.findLastVisibleItemPosition()

            if(visibleLastItemPos == (gameBoxAdapter.itemCount-1)){
                (recyclerView.findViewHolderForLayoutPosition(visibleLastItemPos) as GameBoxAdapter.ViewHolder).apply {
                    itemView.add_boost_app.visibility = View.VISIBLE
                    itemView.app_name.visibility = View.VISIBLE
                }
            }
        }
    }
}