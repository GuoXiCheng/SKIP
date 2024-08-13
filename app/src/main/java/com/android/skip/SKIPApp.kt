package com.android.skip

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils

class SKIPApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var myPackageName: String
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        LogUtils.getConfig()
            .setLogSwitch(true)       // 是否输出日志开关
            .setConsoleSwitch(true)   // 是否在控制台输出日志开关
            .setGlobalTag("SKIP_APP")   // 全局标签
            .setLog2FileSwitch(true)      // 是否写入日志文件开关
            .setDir("")               // 日志文件目录，默认为空，放在 /storage/emulated/0/Android/data/com.android.skip/cache/logs/
            .setFilePrefix("log")     // 日志文件前缀
            .setBorderSwitch(true)    // 日志边框开关
            .setConsoleFilter(LogUtils.V) // 控制台过滤器
            .setFileFilter(LogUtils.V)    // 文件过滤器
            .setStackDeep(1)          // 栈深度

        context = applicationContext
        myPackageName = this.packageName
    }
}