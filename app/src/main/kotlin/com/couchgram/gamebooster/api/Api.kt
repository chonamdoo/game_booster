package com.couchgram.gamebooster.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Created by chonamdoo on 2017. 5. 4..
 */
object Api {
    private val SERVER_CONNTECT_TIME: Long = 10000
    private val API_PUBNATIVE_SERVER_URL = "http://api.ad.couchgram.com"
    private val API_GOOGLE_SERVER_URL = "https://us-central1-eighth-azimuth-817.cloudfunctions.net"

    val adsReqService: RequestService = initAdsRequestService(httpClient())
    val apiReqService: RequestService = initAdsRequestService(httpClient(),API_GOOGLE_SERVER_URL)
    val trackReqService :(String) -> RequestService ={
        initAdsRequestService(httpClient(),it)
    }

    private fun initAdsRequestService(httpClient: OkHttpClient,url : String): RequestService {
        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val service = retrofit.create<RequestService>(RequestService::class.java)
        return service
    }

    private fun initAdsRequestService(httpClient: OkHttpClient): RequestService {
        val retrofit = Retrofit.Builder()
                .baseUrl(API_PUBNATIVE_SERVER_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val service = retrofit.create<RequestService>(RequestService::class.java)
        return service
    }

    private fun httpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
                        .readTimeout(SERVER_CONNTECT_TIME, TimeUnit.MILLISECONDS)
                        .writeTimeout(SERVER_CONNTECT_TIME, TimeUnit.MILLISECONDS)
                        .connectTimeout(SERVER_CONNTECT_TIME, TimeUnit.MILLISECONDS)
                        .addInterceptor(loggingInterceptor).build()
        return httpClient
    }
}