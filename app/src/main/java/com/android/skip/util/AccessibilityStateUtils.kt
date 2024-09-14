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
        val context = MyApp.context
        val serviceNameList = listOf(
            "com.android.skip/.service.MyAccessibilityService",
            "com.android.skip/com.android.skip.service.MyAccessibilityService"
        )

        var accessibilityEnabled = 0
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            LogUtils.e(e)
            return false
        }

        // 检查无障碍服务是否启用
        if (accessibilityEnabled == 1) {
            val settingsValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )

            // 检查启用的无障碍服务列表是否包含目标服务
            if (!settingsValue.isNullOrEmpty()) {
                val simpleStringSplitter = TextUtils.SimpleStringSplitter(':')
                simpleStringSplitter.setString(settingsValue)
                while (simpleStringSplitter.hasNext()) {
                    val accessibilityService = simpleStringSplitter.next()
                    if (serviceNameList.contains(accessibilityService)) {
                        return true
                    }
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