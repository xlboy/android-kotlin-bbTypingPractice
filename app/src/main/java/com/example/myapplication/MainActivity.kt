package com.example.myapplication

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Accessi.AccessibilityService
import com.example.myapplication.Util.Util
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),TextView.OnEditorActionListener {

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        proBtn.setOnClickListener { //打开软件的权限设置页面
            Util().toSelfSetting(this)
        }
        wzaBtn.setOnClickListener { // 打开无障碍设置的页面
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        qunBtn.setOnClickListener { // 跳转至群聊
            Util().joinQQGroup("7fq6cTL0Agu80hSC9Xd9GVYlz1K9VzOY")
        }
        startGdq.setOnClickListener {
            App().showFloatingWindow()
        }
        closeGdq.setOnClickListener {
            if(App.isFloatingWindow == true){
                App.windowManager?.removeView(App.view)
                App.isFloatingWindow = false
                Util().Toast("感谢主人的无情抛弃,886")
            }else{
                Util().Toast("主人稍微成魔就先入佛,行不得")
            }
        }
        access.setOnClickListener {
            startService(Intent(this, AccessibilityService::class.java)) // 开启无障碍模式
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
         return true
    }


}
