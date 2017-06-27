package com.couchgram.gamebooster.data.source

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by chonamdoo on 2017. 4. 28..
 */

data class BoostAppInfo(val packageName: String,val appName: String ,var isAddItem: Boolean = false, var addTime: String="0") : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BoostAppInfo> = object : Parcelable.Creator<BoostAppInfo> {
            override fun createFromParcel(source: Parcel): BoostAppInfo = BoostAppInfo(source)
            override fun newArray(size: Int): Array<BoostAppInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    1 == source.readInt(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(packageName)
        dest.writeString(appName)
        dest.writeInt((if (isAddItem) 1 else 0))
        dest.writeString(addTime)
    }
}