package com.couchgram.gamebooster.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.couchgram.gamebooster.R

/**
 * Created by chonamdoo on 2017. 5. 1..
 */


class LoadingDialog(context : Context,style : Int = R.style.loading_dialog) : Dialog(context, style){
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.let {
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setContentView(R.layout.rocket_progress)
    }
}

/*
class LoadingDialog : Dialog{
    constructor(context : Context) : this(context, R.style.loading_dialog)
    constructor(ctx: Context, style : Int) : super(ctx, style) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_dialog)

    }
}*/
