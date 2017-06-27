package com.couchgram.gamebooster.util

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*


/**
 * Created by chonamdoo on 2017. 5. 24..
 */


object ActivityManager {

    private val activityStack = Stack<WeakReference<Activity>>()

    fun removeActivity(find: Activity) {
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val activity = item.get()
            if (activity == null) {
                iterator.remove()
            } else if (activity === find) {
                iterator.remove()
                break
            }
        }
    }

    fun getActivity(className: String): Activity? {
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()

            val activity = item.get()
            if (activity == null) {
                iterator.remove()
            } else {
                val curName = activity.javaClass.name
                if (curName.compareTo(className) == 0)
                    return activity
            }
        }
        return null
    }

    fun addActivity(activity: Activity) {
        activityStack.add(WeakReference<Activity>(activity))
    }

    fun finishAllActivity() {
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val activity = item.get()
            activity?.let {
                if(!it.isFinishing){
                    try {
                        it.finishAffinity()
                    } catch (e: Exception) {
                        it.finish()
                    }
                }
            }
        }
        activityStack.clear()
    }

    fun finishAllActivity(ignoreActivity: Activity){
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val activity = item.get()
            activity?.let {
                if(it != ignoreActivity && !it.isFinishing){
                    it.finish()
                    iterator.remove()
                }
            }?:iterator.remove()
        }
    }
    fun finishActivity(targetActivityClass: Class<*>){
        val iterator = activityStack.iterator()
        loop@ while (iterator.hasNext()) {
            val item = iterator.next()
            val activity = item.get()
            activity?.let {
                if(it.javaClass.canonicalName == targetActivityClass.canonicalName && !it.isFinishing){
                    try{
                        it.finish()
                        iterator.remove()
                    }catch (e : Exception){
                        e.stackTrace
                    }
                }
            }
        }
    }
}