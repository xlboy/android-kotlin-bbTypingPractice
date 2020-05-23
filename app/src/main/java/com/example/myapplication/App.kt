package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.myapplication.Js.JsObject
import com.example.myapplication.Util.DoubleClickListener
import com.example.myapplication.Util.Base64ImgDownPic
import com.example.myapplication.Util.Util
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class App:Application(){

    companion object {
        var  _context:Application? = null
        var context: Context? =null
        var SevernText:String = ""
        var SevernSection:String = ""
        var view: View?=null
        val url:String = "file:///android_asset/bb-typing/index.html"
//        val url:String = "http://192.168.1.103:9020/"
        var windowManager: WindowManager? = null
        var layoutParams: WindowManager.LayoutParams? = null
        var emit:EditText? = null // emit这个
        var mMainHandler: Handler? =null // 通知是否获取焦点的
        var inputFocusHandler: Handler? =null // 给那个输入框获取焦点
        var mMainWebReloadHandler: Handler? =null // 给那个输入框获取焦点
        var sendMsgQQ:Handler?=null // 发送信息
        var setEditText:Handler?=null // 设置输入框的内容
        var isGd:Boolean?=null // 是否在跟打
        var isAccessi:Boolean=false
        var isFloatingWindow:Boolean=false // 记录悬浮窗的显示状态
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        _context = this
        context=this
        initFloatWindow() // 初始化一波跟打器窗口
        Fresco.initialize(context) // 初始化那个toast
//        Notificat().promess(this) // 检查通知栏的权限，用于弹出信息
        x5Create() // 加载下x5内核
    }

    fun initFloatWindow(){
        windowManager = context!!.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager? // 获取WindowManager服务

        view =LayoutInflater.from(applicationContext).inflate(R.layout.web_activity,null) // 新建悬浮窗控件,用web-activity这个layou

        val webViewXX= view!!.findViewById<WebView>(com.example.myapplication.R.id.webViewX) // 拿到layou里的webView控件，并进行设置

        view!!.findViewById<ImageView>(com.example.myapplication.R.id.gknmlgb) // 双击隐藏显示
            .setOnClickListener(object : DoubleClickListener(){
                override fun onDoubleClick(v: View?) {
                    val y=App.layoutParams!!.y
                    val mConfiguration = App.context!!.getResources().getConfiguration();
                    val ori = mConfiguration.orientation;
                    if(layoutParams!!.height==110){ // 恢复原样
                        layoutParams!!.width = Util().getWidth()
                        layoutParams!!.height = (Util().getHeight() / 2).toInt()
                        if (ori == Configuration.ORIENTATION_LANDSCAPE) { //横屏
                            layoutParams!!.height =(Util().getHeight() / 1.5).toInt()
                        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {}//竖屏
                        layoutParams!!.x =  -(Util().getWidth() / 2)
                        layoutParams!!.y = (y +layoutParams!!.height/ 2 -55)
                    }else{
                        layoutParams!!.width = 195
                        layoutParams!!.y =  (y-layoutParams!!.height / 2 + 55)
                        layoutParams!!.height = 110

                        val msg_=Message()
                        msg_.obj = "不获取"
                        mMainHandler!!.sendMessage(msg_)
                    }

                    windowManager?.updateViewLayout(view,layoutParams)
                }

            })

        view!!.findViewById<ImageView>(com.example.myapplication.R.id.gknmlgb)
            .setOnTouchListener(object : View.OnTouchListener{
                private var x = 0
                private var y = 0
                override fun onTouch(view: View?, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            x = event.rawX.toInt()
                            y = event.rawY.toInt()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val nowX = event.rawX.toInt()
                            val nowY = event.rawY.toInt()
                            val movedX = nowX - x
                            val movedY = nowY - y
                            x = nowX
                            y = nowY
                            layoutParams!!.x = layoutParams!!.x + movedX
                            layoutParams!!.y = layoutParams!!.y + movedY
                            windowManager?.updateViewLayout(App.view, layoutParams)
                        }
                        MotionEvent.ACTION_UP ->{ // 这个是抬起来的
                        }
                    }
                    return false
                }

            })
        emit=view!!.findViewById<EditText>(R.id.editcnm)
        mMainHandler =
            @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    if(msg.obj.toString()=="获取"){
                        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        windowManager!!.updateViewLayout(view,layoutParams)
                        emit!!.setFocusable(true);
                        emit!!.setFocusableInTouchMode(true);
                        emit!!.requestFocus();
                        Handler(Looper.getMainLooper()).postDelayed({
                            val inputManager =
                                emit!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            inputManager?.showSoftInput(emit, 0)
                        }, 100)
                    }else{
                        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        windowManager!!.updateViewLayout(view,layoutParams)
                    }
                }
            }
        inputFocusHandler=
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    windowManager!!.updateViewLayout(view,layoutParams)
                }
            }
        mMainWebReloadHandler=
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    webViewXX.reload()
                }
            }
        setEditText =
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    emit!!.setText("")
                }
            }
        emit!!.addTextChangedListener(object : TextWatcher{ // 将改变的字符传进webview里
            override fun afterTextChanged(s: Editable?) {
                webViewXX.loadUrl("javascript:setText('${emit!!.text}')")

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        webViewXX!!.getSettings().setJavaScriptEnabled(true)
        webViewXX.getSettings().setDomStorageEnabled(true);
        webViewXX.getSettings().setAppCacheMaxSize(1024*1024*8);
        val appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webViewXX.getSettings().setAppCachePath(appCachePath);
        webViewXX.getSettings().setAllowFileAccess(true);
        webViewXX.getSettings().setAppCacheEnabled(true);
        webViewXX.setBackgroundColor(0)
        webViewXX.getBackground().setAlpha(0)
        webViewXX.setLayerType(WebView.LAYER_TYPE_NONE, null)
        webViewXX.requestFocus()
        webViewXX.loadUrl(url)
        webViewXX.addJavascriptInterface(JsObject(context!!),"androids")
        webViewXX.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }
            override fun onReceivedError(var1: WebView?, var2: Int, var3: String?, var4: String?) {
                Log.i("app", "网页加载失败")
            }

        })
        webViewXX.setOnLongClickListener(object : View.OnLongClickListener { // 长按图片就保存
            override fun onLongClick(p0: View?): Boolean {
                val hitTestResult: WebView.HitTestResult = webViewXX.getHitTestResult()
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.type === WebView.HitTestResult.IMAGE_TYPE ||
                    hitTestResult.type === WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
                ) {
                    val url = hitTestResult.extra
                    Base64ImgDownPic().savePicture(context,url)
                }
                return true
            }

        })
        layoutParams = WindowManager.LayoutParams() // 设置LayoutParam
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams!!.format = PixelFormat.RGBA_8888
        layoutParams!!.width =  Util().getWidth()

        layoutParams!!.height = (Util().getHeight() / 2).toInt()
        layoutParams!!.x = 100
        layoutParams!!.y = 300
        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

    }
    @SuppressLint("ShowToast","ClickableViewAccessibility", "ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    fun showFloatingWindow() {
        if (Settings.canDrawOverlays(context)) {
            if(isFloatingWindow) return Util().Toast("主人已开启跟打器了呢，请勿重复操作呢..")
            isFloatingWindow=true // 记录开启了悬浮窗
            windowManager?.addView(view, layoutParams)
            Util().Toast("开启跟打器成功!")
        }else{
            Util().Toast("主人请出门右转设置下悬浮窗权限..")
        }
    }

    fun x5Create(){
        val cb = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                Log.d("app", " X5加载结果 $arg0")
            }
            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        QbSdk.initX5Environment(applicationContext, cb)//x5内核初始化接口
    }


}

