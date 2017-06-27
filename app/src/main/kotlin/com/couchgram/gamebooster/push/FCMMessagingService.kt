package com.couchgram.gamebooster.push

import android.widget.Toast
import com.couchgram.gamebooster.AppContext
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by chonamdoo on 2017. 5. 17..
 */
class FCMMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage?) {
        /*message?.let {
            Completable.fromAction {
                Toast.makeText(AppContext.getInstance(),"메세지 :${message.data}",Toast.LENGTH_LONG).show()
            }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()

        }*/
    }
}