package com.android.skip.util

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.android.skip.MyApp
import com.android.skip.R
import com.blankj.utilcode.util.LogUtils

object AppBasicInfoUtils {
    fun getAppName(packageName: String): String {
        return try {
            val context = MyApp.context
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(applicationInfo).toString()
            appName
        } catch (e: PackageManager.NameNotFoundException) {
            "com.unknown.app"
        }
    }

    fun getAppIcon(packageName: String): Drawable {
        val context = MyApp.context
        return try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            appInfo.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtils.e(e)
            ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)
                ?: context.applicationInfo.loadIcon(context.packageManager)
        }
    }
}