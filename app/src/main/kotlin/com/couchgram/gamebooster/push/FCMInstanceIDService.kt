package com.couchgram.gamebooster.push

import com.couchgram.gamebooster.util.LogUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by chonamdoo on 2017. 5. 17..
 */
class FCMInstanceIDService : FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        LogUtils.v("DEBUG700","refreshedToken : $refreshedToken")
    }
}