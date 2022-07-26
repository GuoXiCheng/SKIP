package com.android.oneclick.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi

class MyAccessibilityService: AccessibilityService() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i("AccessibilityService", "onAccessibilityEvent")
        if (rootInActiveWindow != null) {
            val rect = Rect()
            val position = getNodeText(rootInActiveWindow, "跳过", rect)
            Log.i("getNodeText", position.toString())
//            click(this, rect.exactCenterX(), rect.exactCenterY())
            click(this, 500f, 1000f)
        }
    }
    override fun onInterrupt() {
        Log.i("AccessibilityService", "onInterrupt")
    }

    private fun getNodeText(accessibilityNodeInfo: AccessibilityNodeInfo, nodeText: String, rect: Rect): Rect {
        val childCounts = accessibilityNodeInfo.childCount
        if (childCounts == 0) {
            if (accessibilityNodeInfo.text != null) {
                if (accessibilityNodeInfo.text.toString().contains(nodeText)) {
                    accessibilityNodeInfo.getBoundsInScreen(rect)
                    return rect
                }
            }
            return rect
        }
        for (i in 0 until childCounts) {
            val node = accessibilityNodeInfo.getChild(i)
            if (node != null) {
                getNodeText(node, nodeText, rect)
            }
        }
        return rect
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun click(accessibilityService: AccessibilityService, x: Float, y:Float) {
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()
        accessibilityService.dispatchGesture(gesture, object: AccessibilityService.GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
            }
        }, null)
    }
}