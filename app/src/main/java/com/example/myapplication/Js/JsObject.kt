package com.example.myapplication.Js

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Message
import android.os.Vibrator
import android.provider.Settings
import android.webkit.JavascriptInterface
import androidx.core.app.NotificationCompat
import com.example.myapplication.App
import com.example.myapplication.Notificat.Notificat
import com.example.myapplication.R
import com.example.myapplication.Util.Base64ImgDownPic
import com.example.myapplication.Util.Util
import com.example.myapplication.WebActivity


class JsObject(val Context_:Context) {
    fun volumeChange(){
        val notificationManager = Context_.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {
            Context_.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }
        val audioManager = Context_.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if(audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL){
            audioManager.setRingerMode(2)
        }
        for(i in 1..8){
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE,0)
        }
    }
    fun vibrator(time:Long){ // 震动一波！
        val vibrator = Context_.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time)
    }

    @JavascriptInterface
    fun volumeVibrator() { // 震动
        volumeChange()
        vibrator(5000)
    }
    @JavascriptInterface
    fun suspendShow(str:String) { // 控制悬浮窗的隐藏
        val y=App.layoutParams!!.y
        val x=App.layoutParams!!.x
        println("X:${App.layoutParams!!.x}Y:${App.layoutParams!!.y}")
        if(str== "1"){ // 1的话是显示
            println("给我显示")
            App.layoutParams!!.width = Util().getWidth()
            App.layoutParams!!.height = (Util().getHeight() / 2.5).toInt()
        }else{
            println("给我隐藏")
            App.layoutParams!!.width = Util().getWidth()
            App.layoutParams!!.height = 100
        }
        App.layoutParams!!.x =  -(Util().getWidth() / 2)
        App.layoutParams!!.y = y
        println("改变了，X${App.layoutParams!!.x},Y${App.layoutParams!!.y}")
        Runnable {
            App.windowManager?.updateViewLayout(
                App.view,
                App.layoutParams
            )
        }

    }
    @JavascriptInterface
    fun sendMsg(title:String,content:String){
        Notificat().sendMes(title,content,Context_)
    }

    @JavascriptInterface
    fun readText():String{
        if(!App.isAccessi) Util().Toast("无障碍模式尚未开启!请进行设置!")
        App.setEditText!!.sendMessage(Message())
        return App.SevernText
    }
    @JavascriptInterface
    fun readNoom():String{
        return App.SevernSection
    }
    @JavascriptInterface
    fun inputFocus(){
        App.inputFocusHandler!!.sendMessage(Message())
    }
    @JavascriptInterface
    fun inputBlur(){
        val msg= Message()
        msg.obj="不获取"
        App.mMainHandler!!.sendMessage(msg)
    }
    @JavascriptInterface
    fun changeFlagFocus(){ // 获取焦点时
        App.isGd=true
        val msg= Message()
        msg.obj="获取"
        App.mMainHandler!!.sendMessage(msg)
    }

    @JavascriptInterface
    fun changeFlagBoce(){ // 放开焦点
        App.isGd=false
        val msg= Message()
        msg.obj="不获取"
        App.mMainHandler!!.sendMessage(msg)

    }
    @JavascriptInterface
    fun sendMsgQQ(str:String){ // 发送QQ信息
        val msg=Message()
        msg.obj=str
        App.sendMsgQQ!!.sendMessage(msg)
    }
    @JavascriptInterface
    fun removeEmidText(){ // 发送QQ信息
        App.setEditText!!.sendMessage(Message())
    }
    @JavascriptInterface
    fun textAnew(){ // 重打，清除输入框
        App.setEditText!!.sendMessage(Message())
        changeFlagFocus()
    }
    @JavascriptInterface
    fun message(str:String){ // 提示呗
        Util().Toast(str)
    }
    @JavascriptInterface
    fun webViewReload(){ // 网页刷新
        App.mMainWebReloadHandler!!.sendMessage(Message())
    }
    @JavascriptInterface
    fun readPasteText():String{ // 读取剪贴板的内容
        val cmb: ClipboardManager = App.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val util:Util=Util()
        if(util.Severn(cmb.text.toString())){
            return "${util.section}-❤-❤-${util.contont}"
        }
        return "1-❤-❤-${cmb.text.toString()}"
    }
    @JavascriptInterface
    fun setPasteText(str:String){ // 读取剪贴板的内容
        val cmb: ClipboardManager = App.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.text=str
    }
    @JavascriptInterface
    fun saveImg(str:String){ // 读取剪贴板的内容
        val isSave = Base64ImgDownPic().savePicture(App.context,str)
        if(isSave == true) message("保存成功")
        else message("请检查存储权限是否已授权")
    }
    @SuppressLint("WrongConstant")
    @JavascriptInterface
    fun natShow(title:String,content:String){
        val mNotificationManager =
            Context_.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(Context_)

        mBuilder.setContentTitle(title)//设置通知栏标题
            .setContentText(content) //设置通知栏显示内容
            .setContentIntent(
                PendingIntent.getActivity(Context_, 0,
                    Intent(Context_, WebActivity::class.java), 16)) //设置通知栏点击意图
//	        .setNumber(number) //设置通知集合的数量
            .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
            .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
            .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
            //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
            .setSmallIcon(R.mipmap.icon)//设置通知小ICON
            mNotificationManager.notify(1, mBuilder.build())
}
}
