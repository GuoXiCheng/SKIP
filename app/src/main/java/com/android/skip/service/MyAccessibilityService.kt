package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.ui.main.start.StartAccessibilityRepository
import com.android.skip.util.AccessibilityState
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var repository: StartAccessibilityRepository

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            val activityName = getActivityName(event)

            activityName ?: return

            LogUtils.d("className: $activityName")
        } catch (e: Exception) {
//            LogUtils.e(e)
        }
    }

    override fun onInterrupt() {
        repository.changeAccessibilityState(AccessibilityState.STOPPED)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository.changeAccessibilityState(AccessibilityState.STARTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.changeAccessibilityState(AccessibilityState.STOPPED)
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        return rootInActiveWindow ?: throw IllegalStateException("No valid root node available")
    }

    private fun isSystemClass(className: String): Boolean {
        return try {
            val clazz = Class.forName(className)
            val packageName = clazz.getPackage()?.name
            packageName != null && (packageName.startsWith("android") || packageName.startsWith("androidx"))
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private fun getActivityName(event: AccessibilityEvent?): String? {
        event ?: return null
        val className = event.className

        className ?: return null
        val classNameStr = className.toString()

        val isSystem = isSystemClass(classNameStr)
        return if (!isSystem) classNameStr else null
    }
}