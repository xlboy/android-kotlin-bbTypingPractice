package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Js.JsObject
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.web_activity.*


class WebActivity : AppCompatActivity() {
    val url:String = "file:///android_asset/gdq/index.html"
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity)
        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult( Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }

        val webSettings = webViewX.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webViewX.setLayerType(WebView.LAYER_TYPE_NONE, null)
        webViewX.loadUrl(url)
        webViewX.addJavascriptInterface(JsObject(this),"androids")
        webViewX.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }
            override fun onReceivedError(var1: WebView?, var2: Int, var3: String?, var4: String?) {
                Log.i("app", "网页加载失败")
            }
        })
    }



}



