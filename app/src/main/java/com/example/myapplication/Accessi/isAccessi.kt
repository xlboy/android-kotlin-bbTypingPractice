package com.example.myapplication.Accessi

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.view.accessibility.AccessibilityManager

class isAccessi{
    @Throws(RuntimeException::class)
    fun isAccessibilityEnabled(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        // 检查AccessibilityService是否开启
        val am =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val isAccessibilityEnabled_flag = am.isEnabled
        var isExploreByTouchEnabled_flag = false
        // 检查无障碍服务是否以语音播报的方式开启
        isExploreByTouchEnabled_flag = isScreenReaderActive(context)
        return  isExploreByTouchEnabled_flag
    }

    private val SCREEN_READER_INTENT_ACTION = "android.accessibilityservice.AccessibilityService"
    private val SCREEN_READER_INTENT_CATEGORY = "android.accessibilityservice.category.FEEDBACK_SPOKEN"
    open fun isScreenReaderActive(context: Context): Boolean { // 通过Intent方式判断是否存在以语音播报方式提供服务的Service，还需要判断开启状态
        val screenReaderIntent = Intent(SCREEN_READER_INTENT_ACTION)
        screenReaderIntent.addCategory(SCREEN_READER_INTENT_CATEGORY)
        val screenReaders =
            context.packageManager.queryIntentServices(screenReaderIntent, 0)
        // 如果没有，返回false
        if (screenReaders == null || screenReaders.size <= 0) {
            return false
        }
        var hasActiveScreenReader = false
        if (Build.VERSION.SDK_INT <= 15) {
            val cr = context.contentResolver
            var cursor: Cursor? = null
            var status = 0
            for (screenReader in screenReaders) {
                cursor = cr.query(
                    Uri.parse(
                        "content://" + screenReader.serviceInfo.packageName
                                + ".providers.StatusProvider"
                    ), null, null, null, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    status = cursor.getInt(0)
                    cursor.close()
                    // 状态1为开启状态，直接返回true即可
                    if (status == 1) {
                        return true
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= 26) { // 高版本可以直接判断服务是否处于开启状态
            for (screenReader in screenReaders) {
                hasActiveScreenReader = hasActiveScreenReader or isAccessibilitySettingsOn(
                    context,
                    screenReader.serviceInfo.packageName + "/" + screenReader.serviceInfo.name
                )
            }
        } else { // 判断正在运行的Service里有没有上述存在的Service
            val runningServices: MutableList<String> = ArrayList()
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                runningServices.add(service.service.packageName)
            }
            for (screenReader in screenReaders) {
                if (runningServices.contains(screenReader.serviceInfo.packageName)) {
                    hasActiveScreenReader = hasActiveScreenReader or true
                }
            }
        }
        return hasActiveScreenReader
    }

    // To check if service is enabled
    open fun isAccessibilitySettingsOn(context: Context, service: String): Boolean {
        val mStringColonSplitter = SimpleStringSplitter(':')
        val settingValue: String = Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }
}

