package com.android.skip.util

import android.provider.Settings
import android.text.TextUtils
import com.android.skip.MyApp
import com.android.skip.service.MyAccessibilityService
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ServiceUtils

enum class AccessibilityState {
    STARTED, STOPPED, FAULTED
}

object AccessibilityStateUtils {
    fun getAccessibilityState(): AccessibilityState {
        val isSettingOn = isAccessibilitySettingsOn()
        val isRunning = isAccessibilitySettingsRunning()

        return when {
            isSettingOn && isRunning -> AccessibilityState.STARTED
            isSettingOn && !isRunning -> AccessibilityState.FAULTED
            else -> AccessibilityState.STOPPED
        }
    }

    /**
     * 判断无障碍服务是否：启用
     */
    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val context = MyApp.context
        val service = context.packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            LogUtils.e(e)
        }
        val simpleStringSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingsValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingsValue != null) {
                simpleStringSplitter.setString(settingsValue)
                while (simpleStringSplitter.hasNext()) {
                    val accessibilityService = simpleStringSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) return true
                }
            }
        }
        return false
    }

    /**
     * 判断无障碍服务是否：可用
     */
    private fun isAccessibilitySettingsRunning(): Boolean {
        return ServiceUtils.isServiceRunning(MyAccessibilityService::class.java)
    }
}