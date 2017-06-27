package com.couchgram.gamebooster

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.couchgram.gamebooster.ui.widget.dialog.LoadingDialog
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by chonamdoo on 2017. 5. 1..
 */

abstract class BaseFragment : RxFragment(){
    private var dialog : LoadingDialog? = null

    @LayoutRes
    abstract fun getLayoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(getLayoutResource(), container, false)

    fun showDialog(activity: BaseActivity?){
        activity?.let {
            dialog?.let {
                if(dialog?.isShowing as Boolean){
                    dialog?.dismiss()
                }
                dialog = null
            }
            dialog = LoadingDialog(activity).apply {
                show()
            }
        }
    }

    fun dismissDialog(){
        dialog?.let{
            dialog?.dismiss()
        }
    }
}