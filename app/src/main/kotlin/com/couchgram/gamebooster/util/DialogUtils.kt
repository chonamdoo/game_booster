package com.couchgram.gamebooster.util

import android.app.Activity
import com.couchgram.gamebooster.ui.widget.dialog.LoadingDialog

/**
 * Created by chonamdoo on 2017. 5. 1..
 */

object DialogUtils{

    private var dialog : LoadingDialog? = null

    fun showDialog(activity: Activity?){
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