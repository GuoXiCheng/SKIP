package com.android.oneclick.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i("AccessibilityService", "onAccessibilityEvent")
    }
    override fun onInterrupt() {
        Log.i("AccessibilityService", "onInterrupt")
    }
}