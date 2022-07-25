package com.android.oneclick.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i("AccessibilityService", "onAccessibilityEvent")
        if (rootInActiveWindow != null) {
            getNodeText(rootInActiveWindow)
        }
    }
    override fun onInterrupt() {
        Log.i("AccessibilityService", "onInterrupt")
    }

    private fun getNodeText(accessibilityNodeInfo: AccessibilityNodeInfo) {
        val childCounts = accessibilityNodeInfo.childCount
        if (childCounts == 0) {
            if (accessibilityNodeInfo.text != null) {
                Log.i("getNodeText", accessibilityNodeInfo.text.toString())
            }
            return
        }
        for (i in 0 until childCounts) {
            val node = accessibilityNodeInfo.getChild(i)
            if (node != null) {
                getNodeText(node)
            }
        }
        return
    }
}