package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class MyAccessibilityService : AccessibilityService() {
    private var currentPackage = ""
    private var scanTime = 0
    private var clickTime = 0
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        val rect = Rect()
        val windowNodes = windows
        val rootNode = rootInActiveWindow
        for (i in 0 until windowNodes.size) {
            val rootWindow = windowNodes[i].root
            if (rootWindow != null && rootNode == rootWindow) {
                scanTime += 1
                if (currentPackage == rootWindow.packageName.toString()) {
                    if (clickTime == 1) return
                    if (scanTime > 30) return
                } else {
                    scanTime = 0
                    clickTime = 0
                }
                val position = getNodeText(rootWindow, rect)
                if (position.toShortString() != "[0,0][0,0]") {
                    click(this, rect.exactCenterX(), rect.exactCenterY())
                    clickTime += 1
                }
                currentPackage = rootWindow.packageName.toString()
            }
        }
    }

    override fun onInterrupt() {
        Log.i("AccessibilityService", "onInterrupt")
    }

    private fun getNodeText(accessibilityNodeInfo: AccessibilityNodeInfo, rect: Rect): Rect {
        val childCounts = accessibilityNodeInfo.childCount

        if (childCounts == 0) {
            if (accessibilityNodeInfo.text != null) {
                val nodeText = accessibilityNodeInfo.text.toString()
                if (nodeText.length <= 6 && nodeText.contains("跳过")) {
                    accessibilityNodeInfo.getBoundsInScreen(rect)
                }
            } else if (accessibilityNodeInfo.contentDescription != null) {
                val content = accessibilityNodeInfo.contentDescription.toString()
                if (content.length <= 6 && content.contains("跳过")) {
                    accessibilityNodeInfo.getBoundsInScreen(rect)
                }
            }
            return rect
        }
        for (i in 0 until childCounts) {
            val node = accessibilityNodeInfo.getChild(i)
            if (node != null) {
                getNodeText(node, rect)
            }
        }
        return rect
    }


    private fun click(accessibilityService: AccessibilityService, x: Float, y: Float) {
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()
        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                    Toast.makeText(accessibilityService, "已为您跳过广告", Toast.LENGTH_SHORT).show()
                }
            },
            null
        )
    }

}