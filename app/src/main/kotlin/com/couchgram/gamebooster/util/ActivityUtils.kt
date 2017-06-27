package com.couchgram.gamebooster.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

/**
 * This provides methods to help Activities load their UI.
 */
object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.

     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }
    fun replaceFragmentToActivity(mainViewId : Int,fragmentManager: FragmentManager,fragment: Fragment, tag : String){
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(mainViewId,fragment,tag)
        transaction.commitAllowingStateLoss()
    }
}
