package com.android.skip

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Path
import android.graphics.Rect
import android.provider.Settings
import android.text.TextUtils
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.ToastManager
import com.android.skip.service.MyAccessibilityService

object MyUtils {
    /**
     * 判断无障碍服务是否已经启用
     */
    fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
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
        }
        return false
    }

    fun isExcludeApplication (appName: String, packageName: String, packageManager: PackageManager): Boolean {
        if (appName == packageName) return true
        return try {
            // 获取应用程序的 ApplicationInfo
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)

            // 判断应用是否来自系统预装或者是用户手动安装的
            (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            // 应用未找到，可能是无效的包名
            true
        }
    }

    fun click(accessibilityService: AccessibilityService, rect: Rect) {
        val path = Path()
        path.reset()
        path.moveTo(rect.exactCenterX(), rect.exactCenterY())
        path.lineTo(rect.exactCenterX(), rect.exactCenterY())

        val builder = GestureDescription.Builder()
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()

        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)

                    if (AnalyticsManager.isShowToast()) {
                        ToastManager.showToast(accessibilityService, "已为您跳过广告")
                        AnalyticsManager.setShowToastCount()
                    }

                }
            },
            null
        )
    }
}