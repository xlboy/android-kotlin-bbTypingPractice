package com.example.myapplication.Util

import android.view.View

internal abstract class DoubleClickListener : View.OnClickListener { // 小乌鸦LOGO显示隐藏的重写
    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            onDoubleClick(v)
        }
        lastClickTime = currentTimeMillis
    }

    abstract fun onDoubleClick(v: View?)

    companion object {
        private const val DOUBLE_TIME: Long = 500
        private var lastClickTime: Long = 0
    }
}
