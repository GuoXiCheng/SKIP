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

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {
            getCurrentRootNode()
        } catch (e: Exception) {
            LogUtils.e(e)
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
}