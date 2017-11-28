package com.example.cdialoglib

/**
 * 点击事件处理
 *
 * @author Chengguo on 2017/11/27.
 */
class CDialogClickUtil {

    companion object {
        var lastClickTime = 0L

        //同步方法  判断是否是快速点击事件
        @Synchronized
        fun synchronisFastClick(): Boolean {
            val time = System.currentTimeMillis()
            if (time - lastClickTime < 500) {
                return true
            }
            lastClickTime = time
            return false
        }
    }
}