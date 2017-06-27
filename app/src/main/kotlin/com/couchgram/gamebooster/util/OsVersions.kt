package com.couchgram.gamebooster.util

import android.os.Build



/**
 * Created by chonamdoo on 2017. 5. 11..
 */
object OsVersions {
    /**
     * OS Version이 Android 4.1 (API 16) 이상인지 확인합니다.

     * @return Version >= JELLY_BEAN 이면 true, 이 외에는 false
     */
    fun isAtLeastJellyBean(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.JELLY_BEAN)
    }

    /**
     * OS Version이 Android 4.2 (API 17) 이상인지 확인합니다.

     * @return Version >= JELLY_BEAN_MR1 이면 true, 이 외에는 false
     */
    fun isAtLeastJellyBeanMR1(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.JELLY_BEAN_MR1)
    }

    /**
     * OS Version이 Android 4.3 (API 18) 이상인지 확인합니다.

     * @return Version >= JELLY_BEAN_MR2 이면 true, 이 외에는 false
     */
    fun isAtLeastJellyBeanMR2(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.JELLY_BEAN_MR2)
    }

    /**
     * OS Version이 Android 4.4 (API 19) 이상인지 확인합니다.

     * @return Version >= KITKAT 이면 true, 이 외에는 false
     */
    fun isAtLeastKitkat(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.KITKAT)
    }

    /**
     * OS Version이 Android 5.0 (API 21) 이상인지 확인합니다.

     * @return Version >= LOLLIPOP 이면 true, 이 외에는 false
     */
    fun isAtLeastLollipop(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.LOLLIPOP)
    }

    /**
     * OS Version이 Android 5.1 (API 22) 이상인지 확인합니다.

     * @return Version >= LOLLIPOP_MR1 이면 true, 이 외에는 false
     */
    fun isAtLeastLollipopMR1(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.LOLLIPOP_MR1)
    }

    /**
     * OS Version이 Android 6.0 (API 23) 이상인지 확인합니다.

     * @return Version >= M 이면 true, 이 외에는 false
     */
    fun isAtLeastM(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.M)
    }

    /**
     * OS Version이 Android 7.0 (API 24) 이상인지 확인합니다.

     * @return Version >= N 이면 true, 이 외에는 false
     */
    fun isAtLeastN(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.N)
    }

    /**
     * OS Version이 Android 7.1.1 (API 25) 이상인지 확인합니다.

     * @return Version >= N_MR1 이면 true, 이 외에는 false
     */
    fun isAtLeastNMR1(): Boolean {
        return greaterOrEquals(Build.VERSION_CODES.N_MR1)
    }

    /**
     * OS Version이 apiLevel 이상인지 확인합니다.

     * @param apiLevel VERSION_CODE
     * *
     * @return Version >= apiLevel 이면 true, 이 외에는 false
     * *
     * @see android.os.Build.VERSION_CODES
     */
    fun greaterOrEquals(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }
}