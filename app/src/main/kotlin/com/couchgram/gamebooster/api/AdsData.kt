package com.couchgram.gamebooster.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.net.URLDecoder

/**
 * Created by chonamdoo on 2017. 5. 4..
 */
class AdsData {
    @SerializedName("build_id")
    @Expose
    var build_id: String? = null

    @SerializedName("platform")
    @Expose
    var platform: String? = null

    @SerializedName("store_rating")
    @Expose
    var store_rating: String? = null

    @SerializedName("buildtotal_ratings_id")
    @Expose
    var total_ratings: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("size")
    @Expose
    var size: String? = null

    @SerializedName("developer")
    @Expose
    var developer: String? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("banner_url")
    @Expose
    var banner_url: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }
    @SerializedName("portrait_banner_url")
    @Expose
    var portrait_banner_url: String? = null

    @SerializedName("cta_text")
    @Expose
    var cta_text: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }
    @SerializedName("description")
    @Expose
    var description: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }
    @SerializedName("icon_url")
    @Expose
    var icon_url: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }

    @SerializedName("title")
    @Expose
    var title: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }

    @SerializedName("horizontal")
    @Expose
    var horizontal: String? = null

    @SerializedName("update_time")
    @Expose
    var update_time: String? = null

    @SerializedName("cid")
    @Expose
    var cid: String? = null

    @SerializedName("click_url")
    @Expose
    var click_url: String? = null

    @SerializedName("global")
    @Expose
    var global: String? = null

    @SerializedName("countries")
    @Expose
    var countries: String? = null

    @SerializedName("points")
    @Expose
    var points: String? = null

    @SerializedName("min_os_version")
    @Expose
    var min_os_version: String? = null

    @SerializedName("device_id_required")
    @Expose
    var device_id_required: String? = null

    @SerializedName("install")
    @Expose
    var install: String? = null

    @SerializedName("click")
    @Expose
    var click: String? = null

    @SerializedName("category_count")
    @Expose
    var category_count: String? = null

    @SerializedName("order_point")
    @Expose
    var order_point: String? = null

    @SerializedName("real_click_url")
    @Expose
    var real_click_url: String? = null
        get() {
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }

    @SerializedName("real_click_log")
    @Expose
    var real_click_log: String? = null
        get(){
            if(!field.isNullOrEmpty()){
                field = URLDecoder.decode(field)
            }
            return field
        }
}