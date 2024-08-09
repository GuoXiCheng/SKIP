package com.android.skip.utils

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import com.android.skip.enums.AccessibilityState
import com.android.skip.service.MyAccessibilityService

object AccessibilityUtils {

    /**
     * 取得无障碍服务的状态
     */
    fun getAccessibilityState(mContext: Context): AccessibilityState {
        val isSettingsOn = isAccessibilitySettingsOn(mContext)
        val isRunning = isAccessibilityServiceRunning(mContext, MyAccessibilityService::class.java)

        return when {
            isSettingsOn && isRunning -> AccessibilityState.STARTED
            isSettingsOn && !isRunning -> AccessibilityState.FAULTED
            else -> AccessibilityState.STOPPED
        }
    }

    /**
     * 判断无障碍服务是否已经启用
     */
    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
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

    /**
     * 判断无障碍服务是否可用
     */
    private fun isAccessibilityServiceRunning(
        mContext: Context,
        serviceClass: Class<out AccessibilityService>
    ): Boolean {
        val am = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = am.getRunningServices(Integer.MAX_VALUE)

        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}