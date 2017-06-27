package com.couchgram.gamebooster.util.fresco.imagepipeline

import android.content.Context
import com.couchgram.gamebooster.util.fresco.ConfigConstants
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import java.util.HashSet

/**
 * Created by chonamdoo on 2017. 4. 27..
 */
object ImagePipelineConfigFactory {
    private val IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache"

    private lateinit var sImagePipelineConfig: ImagePipelineConfig
    private lateinit var sOkHttpImagePipelineConfig: ImagePipelineConfig
    fun getImagePipelineConfig(context: Context): ImagePipelineConfig {
        val configBuilder = ImagePipelineConfig.newBuilder(context)
        configureCaches(configBuilder, context)
        configureLoggingListeners(configBuilder)
        configureOptions(configBuilder)
        sImagePipelineConfig = configBuilder.build()
        return sImagePipelineConfig
    }

    fun getOkHttpImagePipelineConfig(context: Context): ImagePipelineConfig {
        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
        val configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
        configureOptions(configBuilder)
        configureCaches(configBuilder, context)
        configureLoggingListeners(configBuilder)
        sOkHttpImagePipelineConfig = configBuilder.build()
        return sOkHttpImagePipelineConfig
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private fun configureCaches(
            configBuilder: ImagePipelineConfig.Builder,
            context: Context) {
        val bitmapCacheParams = MemoryCacheParams(
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE, // Max entries in the cache
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE, // Max length of eviction queue
                Integer.MAX_VALUE)                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier { bitmapCacheParams }
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setBaseDirectoryPath(context.applicationContext.cacheDir)
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)
                                .build())
    }

    private fun configureLoggingListeners(configBuilder: ImagePipelineConfig.Builder) {
        val requestListeners = HashSet<RequestListener>()
        requestListeners.add(RequestLoggingListener())
        configBuilder.setRequestListeners(requestListeners)
    }

    private fun configureOptions(configBuilder: ImagePipelineConfig.Builder) {
        configBuilder.isDownsampleEnabled = true
    }

}