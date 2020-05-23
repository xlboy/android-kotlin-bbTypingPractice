package com.example.myapplication.Accessi

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.myapplication.App
import com.example.myapplication.Util.Util


@SuppressLint("Registered")
class AccessibilityService :AccessibilityService(){
    var mService: com.example.myapplication.Accessi.AccessibilityService? = null
    var inputBox :AccessibilityNodeInfo? =null
    var pageName :String? =null
    var sendBtnBox :AccessibilityNodeInfo? =null
    private val TAG = javaClass.name
    var rowNode = rootInActiveWindow
    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pageStr= event!!.getPackageName()
        val type=event.getEventType()
        if(pageStr=="com.android.systemui" || App.isGd ==true) return
        rowNode = rootInActiveWindow
        if (rowNode == null) {
            Log.i(TAG, "noteInfo is　null")
            return
        } else {
            recycle(rowNode,"android.widget.TextView","com.tencent.mobileqq:id/chat_item_content_layout")
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("无障碍模式开启成功")
        App.isAccessi =true
        App.sendMsgQQ =@SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val arguments = Bundle()
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg.obj.toString()) // +"\uF37A\uF37A"
                Handler(Looper.getMainLooper()).postDelayed({
                    inputBox?.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                }, 600)
                Handler(Looper.getMainLooper()).postDelayed({
                    sendBtnBox?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }, 1000)
            }
        }
        mService=this
    }
    fun isStart(): Boolean { // 判断无障碍模式是否运行中
        return mService != null
    }
    fun recycleClick(info: AccessibilityNodeInfo){ // 模拟点击
        if (info.childCount == 0) {
            if(info.className=="android.widget.Button" && info.text=="发送"){
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return
            }
        } else {
            for (i in 0 until info.childCount) {
                if (info.getChild(i) != null) {
                    recycleClick(info.getChild(i))
                }
            }
        }
    }
    fun recycleText(info: AccessibilityNodeInfo,str:String){ // 获取文段
        if (info.childCount == 0) {
            if(info.className=="android.widget.EditText" ){
                val arguments = Bundle()
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, str)
                info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                return
            }
        } else {
            for (i in 0 until info.childCount) {
                if (info.getChild(i) != null) {
                    recycleText(info.getChild(i),str)
                }
            }
        }
    }
    fun recycle(info: AccessibilityNodeInfo,strType:String,strId:String) { // 遍历所有控件
        if (info.childCount == 0) {
            if(info.text!=null && info.viewIdResourceName!=null){ // 抓到这两玩意，然后进行发送
                 //if(info.viewIdResourceName.toString()=="com.tencent.mobileqq:id/input"){
                if(info.className=="android.widget.EditText"){
                        inputBox=info
                 }else if(info.className=="android.widget.Button" && info.text=="发送"){
                        sendBtnBox=info
                 }
            }
            if(info.className == strType && info.text!=null){ // 若是text组件，并且值不等于空，判断是否属于赛文格式
                var util: Util = Util()
                if(util.Severn(info.text.toString())){
                    App.SevernSection =util.section
                    App.SevernText =util.contont
                }
            }
        } else {
            for (i in 0 until info.childCount) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i),strType,strId)
                }
            }
        }
    }
}