package com.example.myapplication.Notificat

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.WebActivity

class Notificat(){

    fun promess(context: Context){ // 检查下权限
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val builder: MaterialDialog? = MaterialDialog.Builder(context)
                .title("请手动将通知打开")
                .positiveText("确定")
                .negativeText("取消")
                .onAny(object : MaterialDialog.SingleButtonCallback {
                    override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                        if (which == DialogAction.NEUTRAL) {
                            Log.e("onClick", "更多信息: ");
                        } else if (which == DialogAction.POSITIVE) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <  Build.VERSION_CODES.O) {
                                var intent = Intent()
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", context.getPackageName());
                                intent.putExtra("app_uid", context.getApplicationInfo().uid);
                                ContextCompat.startActivity(context, intent, null)
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                                var intent = Intent()
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                ContextCompat.startActivity(context, intent, null)
                            } else {
                                var localIntent =  Intent()
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                ContextCompat.startActivity(context, localIntent, null)
                            }
                            Log.e("onClick", "同意: ");
                        } else if (which == DialogAction.NEGATIVE) {
                            Log.e("onClick", "不同意: ");
                        }
                    }
                }).show()
        }
    }

    fun sendMes(title:String,text:String,context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "chat";//设置通道的唯一ID
            val channelName = "聊天消息";//设置通道名
            val importance = NotificationManager.IMPORTANCE_HIGH;//设置通道优先级
            createNotificationChannel(context,channelId, channelName, importance,title,text);
        } else {
            sendSubscribeMsg(title,text,context)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId:String , channelName:String, importance:Int,title:String , text:String) {
        var channel= NotificationChannel(channelId, channelName, importance)
        var notificationManager: NotificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager;
        notificationManager.createNotificationChannel(channel)
        sendSubscribeMsg(title,text,context)
    }
    @SuppressLint("WrongConstant")
    fun sendSubscribeMsg(title:String, text:String, context: Context) {
        val manager:NotificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager;

        val intent =Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(ComponentName(context, WebActivity::class.java))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        val notification: Notification =  NotificationCompat.Builder(context,"chat")
            .setContentTitle(title)
            .setContentText(text)

            .setContentIntent(
                PendingIntent.getActivity(context, 0,
                    intent, 0)) //设置通知栏点击意图
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.icon)
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)

            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.icon
            ))
            .setAutoCancel(true)
            .build()
        manager.notify(1, notification);
    }
}