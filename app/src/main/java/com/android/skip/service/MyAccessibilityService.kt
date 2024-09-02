package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.blankj.utilcode.util.LogUtils

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {
            getCurrentRootNode()
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        return rootInActiveWindow ?: throw IllegalStateException("No valid root node available")
    }
}