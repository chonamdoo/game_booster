package com.couchgram.gamebooster.util

import android.util.Log
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by chonamdoo on 2017. 4. 26..
 */

class CustomScheduledExecutor(private val source: String, doKeepAlive: Boolean) {
    private val executor: ScheduledThreadPoolExecutor
    init {

        executor = ScheduledThreadPoolExecutor(1/*Runtime.getRuntime().availableProcessors()+1*/, // Single thread
                ThreadFactory { runnable ->
                    // Creator of daemon threads
                    val thread = Executors.defaultThreadFactory().newThread(RunnableWrapper(runnable))

                    thread.priority = Thread.MIN_PRIORITY
                    thread.name = "couch" + thread.name + "-" + source
                    thread.isDaemon = true

                    thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler {
                        th, tr -> Log.v(TAG, String.format("Thread %s with error %s", th.name, tr.message))
                    }
                    thread
                }, RejectedExecutionHandler { runnable, executor ->
            // Logs rejected runnables rejected from the entering the pool
            Log.v(TAG, String.format("Runnable %s rejected from %s ", runnable.toString(), source))
        }
        )

        if (!doKeepAlive) {
            executor.setKeepAliveTime(10L, TimeUnit.MILLISECONDS)
            executor.allowCoreThreadTimeOut(true)
        }
    }

    fun submit(task: Runnable): Future<*> {
        return executor.submit(task)
    }

    fun shutdownNow() {
        executor.shutdownNow()
    }

    fun scheduleWithFixedDelay(command: Runnable, initialDelay: Long, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit)
    }

    fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return executor.schedule(command, delay, unit)
    }

    private inner class RunnableWrapper (private val runnable: Runnable): Runnable {

        override fun run() {
            try {
                runnable.run()

            } catch (t: Throwable) {
                Log.v(TAG, String.format("Runnable error %s", t.message))
            }

        }
    }

    companion object {
        private val TAG = "CustomScheduledExecutor"
    }
}