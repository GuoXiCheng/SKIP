package com.android.skip

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        context = this
        deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"

        DataStoreUtils.init(this)

        Utils.init(this)
        LogUtils.getConfig()
            .setLogSwitch(false)                    // 是否输出日志开关
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

        lateinit var deviceName: String
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}