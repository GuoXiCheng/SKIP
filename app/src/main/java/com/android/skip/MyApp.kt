package com.android.skip

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this

        Utils.init(this)
        LogUtils.getConfig()
            .setLogSwitch(true)                    // 是否输出日志开关
            .setConsoleSwitch(true)                // 是否在控制台输出日志开关
            .setGlobalTag("SKIP_APP")              // 全局标签
            .setLog2FileSwitch(true)               // 是否写入日志文件开关
            .setDir("${this.cacheDir}/logs")       // 日志文件目录
            .setFilePrefix("log")                  // 日志文件前缀
            .setBorderSwitch(true)                 // 日志边框开关
            .setConsoleFilter(LogUtils.V)          // 控制台过滤器
            .setFileFilter(LogUtils.V)             // 文件过滤器
            .setStackDeep(1)                       // 栈深度
            .setSaveDays(7)                        // 日志保留天数
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}