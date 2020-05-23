package com.example.myapplication.Util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import java.io.*
import java.net.URL


class Base64ImgDownPic {

    fun savePicture(context: Context?, base64DataStr: String): Boolean {

        // 1.去掉base64中的前缀
        val base64Str =
            base64DataStr.substring(base64DataStr.indexOf(",") + 1, base64DataStr.length)
        // 首先保存图片
        val appDir =
            File(Environment.getExternalStorageDirectory(), "bb-typing-img") // "abc":图片保存的文件夹的名称

        if (!appDir.exists()) {
            appDir.mkdir()
        }
        // 2.拼接图片的后缀，根据自己公司的实际情况拼接，也可从base64中截取图片的格式。
        val imgName = System.currentTimeMillis().toString() + ".png"
        val fileTest = File(appDir, imgName)
        // 3. 解析保存图片
        val data: ByteArray = Base64.decode(base64Str, Base64.DEFAULT)
        for (i in data.indices) {
            if (data[i] < 0) {
                data[i]= (data[i] + 256.toByte()).toByte()
            }
        }
        var os: OutputStream? = null
        return try {
            os = FileOutputStream(fileTest)
            os.write(data)
            os.flush()
            os.close()
            // 4. 其次通知系统刷新图库
            updateAlbum(context!!, fileTest)
//            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show()
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    private fun updateAlbum(context: Context, file: File) {
        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }
}