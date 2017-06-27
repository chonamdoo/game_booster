package com.couchgram.gamebooster.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.couchgram.gamebooster.R
import kotlinx.android.synthetic.main.dialog_confirm.*

/**
 * Created by chonamdoo on 2017. 5. 22..
 */
class ConfirmDialog @JvmOverloads constructor(
        context : Context,
        themeResourceId : Int = 0
) : Dialog(context,themeResourceId), View.OnClickListener {
    private var positiveClickListener : View.OnClickListener?= null
    private var negativeClickListener: View.OnClickListener?= null
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.let {
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setContentView(R.layout.dialog_confirm)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        txt_confirm_title.text = context.getString(R.string.add_shortcut)
        txt_confirm_comment.text = context.getString(R.string.add_shortcut_desc)
        dialog_cancel.text = context.getString(R.string.close)
        dialog_confirm.text = context.getString(R.string.add)
        dialog_cancel.setOnClickListener(this)
        dialog_confirm.setOnClickListener(this)

    }
    fun setTitleText(title : String){
        txt_confirm_title.text = title
    }
    fun setContentText(content : String){
        txt_confirm_comment.text = content
    }
    fun setCancleText(cancel : String){
        dialog_cancel.text = cancel
    }
    fun setConfirmText(confirmText : String){
        dialog_confirm.text = confirmText
    }
    override fun onClick(v: View?) {
        v?.let {
            when(it.id){
                R.id.dialog_cancel ->{
                    negativeClickListener?.onClick(it)
                    dismiss()
                }
                else -> {
                    positiveClickListener?.onClick(it)
                    dismiss()
                }
            }
        }
    }
    fun setPositiveClickListener(positiveClickListener : View.OnClickListener){
        this.positiveClickListener = positiveClickListener
    }

    fun setNegativeClickListener(negativeClickListener : View.OnClickListener){
        this.negativeClickListener = negativeClickListener
    }
    fun setDialogClickListener(negativeClickListener : View.OnClickListener, positiveClickListener : View.OnClickListener){
        this.positiveClickListener = positiveClickListener
        this.negativeClickListener = negativeClickListener
    }
}