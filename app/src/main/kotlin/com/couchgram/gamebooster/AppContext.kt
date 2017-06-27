package com.couchgram.gamebooster

import android.content.Context
import android.content.ContextWrapper

/**
 * Created by chonamdoo on 2017. 3. 29..
 */
class AppContext private constructor(context: Context) : ContextWrapper(context) {
    companion object {
        lateinit var minstance: AppContext

        @JvmStatic fun init(context: Context) {
            minstance = AppContext(context.createPackageContext(context.packageName, Context.CONTEXT_INCLUDE_CODE))
        }

        @JvmStatic fun getInstance(): AppContext {
            return minstance
        }

    }

}

