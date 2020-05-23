package com.example.myapplication.Util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.App
import xyz.bboylin.universialtoast.UniversalToast


class Util {
    var contont = "" // 获取到的赛文
    var section = "" // 获取到的段数
    fun getWidth():Int{ // 获取屏幕的宽度
        return (App._context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.width
    }
    fun getHeight():Int{ // 获取屏幕的高度
        return (App._context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.height
    }
    fun Toast(str:String){ // 封装好的Toast弹框
        UniversalToast.makeText(App.context!!,str,UniversalToast.LENGTH_SHORT,UniversalToast.UNIVERSAL).show()
    }
    fun Severn(str:String):Boolean{ // 判断是否为赛文
        val listStr = str.split("\n")
        var listLenth = 0
        listStr.forEach{
            val result=entStrVerif(it)
            if(result !=""){
                section=result
                contont=listStr[listLenth-1]
                return true
            }
            listLenth++
        }
        if(listLenth < 1) return false
        section=entStrVerif(listStr[listLenth-1])
        if(section != ""){
            contont=listStr[listLenth-2]
            return true
        }else if(listLenth>3){
            section=entStrVerif(listStr[listLenth-2])
            if(section != ""){
                contont=listStr[listLenth-3]
                return true
            }
        }
        return false
    }
    fun entStrVerif(str:String):String{
        val r = Regex("-{5}第\\d+段")
        if(!r.containsMatchIn(str)) return ""
        val jg=r.find(str)?.value.toString()
        return Regex("[^\\d]").replace(jg,"")
    }
    fun toSelfSetting(context: Context) { // 跳转到软件权限设置
        val mIntent = Intent()
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            mIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.action = Intent.ACTION_VIEW
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails")
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(mIntent)
    }
    fun joinQQGroup(key: String) {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(App.context!!,intent,null)
    }
    fun getDefaultInputMethodPkgName(context:Context):String { // 获取当前的输入法列表
        var mDefaultInputMethodPkg: String? = null

        val mDefaultInputMethodCls = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD)
        //输入法类名信息
        Log.d(ContentValues.TAG, "mDefaultInputMethodCls=" + mDefaultInputMethodCls);
        if (!TextUtils.isEmpty(mDefaultInputMethodCls)) {
            mDefaultInputMethodPkg =mDefaultInputMethodCls.toString().split("/")[0]
        }
        return mDefaultInputMethodPkg.toString()
        //        var imm:InputMethodManager =
//            this.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
//        var  methodList:List<InputMethodInfo> = imm.getInputMethodList();
//        for(i in methodList) {
//            val value: Any  = i.loadLabel (getPackageManager())
//            println("拿到了一个输入法${value}")
//        }
    }
}