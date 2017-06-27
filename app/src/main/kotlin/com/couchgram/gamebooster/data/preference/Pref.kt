package com.couchgram.gamebooster.data.preference

import com.marcinmoskala.kotlinpreferences.PreferenceHolder

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

object Pref : PreferenceHolder(){
    var advertiseId : String by bindToPreferenceField("","advertise_id")
    var isDevelopMode : Boolean by bindToPreferenceField(false,"isdevelmode")
    var packageName : String by bindToPreferenceField("","packagename")
    var once_add_short_cut : Boolean by bindToPreferenceField(false,"once_add_short_cut")
    var floatingX : Int by bindToPreferenceField(0,"floatingX")
    var floatingY : Int by bindToPreferenceField(0,"floatingY")
    var call_window_mode : Boolean by bindToPreferenceField(false,"call_window_mode")
}