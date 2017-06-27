package com.couchgram.gamebooster.util.daemon

import android.content.Context
import android.os.Build
import com.couchgram.gamebooster.util.LogUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by chonamdoo on 2017. 5. 1..
 */
object Command {
    private val TAG = Command::class.java.simpleName

    /**
     * copy file to destination
     */
    @Throws(IOException::class, InterruptedException::class)
    private fun copyFile(file: File, inputStream: InputStream, mode: String) {
        val abspath = file.absolutePath
        val out = FileOutputStream(file)
        inputStream.use {inputStream ->
            out.use {fileOutputStream ->
                inputStream.copyTo(fileOutputStream)
            }
        }
        Runtime.getRuntime().exec("chmod $mode $abspath").waitFor()
    }

    /**
     * copy file in assets into destination file

     * @param context        context
     * *
     * @param assetsFilename file name in assets
     * *
     * @param file           the file to copy to
     * *
     * @param mode           mode of file
     * *
     * @throws IOException
     * *
     * @throws InterruptedException
     */
    @Throws(IOException::class, InterruptedException::class)
    fun copyAssets(context: Context, assetsFilename: String, file: File, mode: String) {
        val manager = context.assets
        val inputStream = manager.open(assetsFilename)
        copyFile(file,inputStream, mode)
    }

    /**
     * Install specified binary into destination directory.

     * @param context  context
     * *
     * @param destDir  destionation directory
     * *
     * @param filename filename of binary
     * *
     * @return true if install successfully, otherwise return false
     */
    fun install(context: Context, destDir: String, filename: String): Boolean {
        var binaryDir = "armeabi"

        val abi = Build.CPU_ABI
        if (abi.startsWith("armeabi-v7a")) {
            binaryDir = "armeabi-v7a"
        } else if (abi.startsWith("x86")) {
            binaryDir = "x86"
        }

        /* for different platform */
        val assetfilename = binaryDir + File.separator + filename

        try {
            val f = File(context.getDir(destDir, Context.MODE_PRIVATE), filename)
            if (f.exists()) {
                LogUtils.d(TAG, "binary has existed")
                return false
            }

            copyAssets(context, assetfilename, f, "0755")
            return true
        } catch (e: Exception) {
            LogUtils.e(TAG, "installBinary failed: ${e.message}")
            return false
        }

    }
}