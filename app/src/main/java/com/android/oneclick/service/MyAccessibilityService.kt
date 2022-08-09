package com.android.oneclick.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService: AccessibilityService() {
    private var currentPackage = ""
    private var scanTime = 0
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        val rect = Rect()
        val windowNodes = windows
        val rootNode = rootInActiveWindow
        for (i in 0 until windowNodes.size) {
            val rootWindow = windowNodes[i].root
            if (rootWindow != null && rootNode == rootWindow) {
                scanTime += 1
                if (currentPackage == rootWindow.packageName.toString()) {
                    if (scanTime > 30) return
                }
                else scanTime = 0
                val position = getNodeText(rootWindow, "跳过", rect)
                if (position.toShortString() != "[0,0][0,0]") {
                    click(this, rect.exactCenterX(), rect.exactCenterY())
                }
                currentPackage = rootWindow.packageName.toString()
            }
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


    private fun click(accessibilityService: AccessibilityService, x: Float, y: Float) {

            val builder = GestureDescription.Builder()
            val path = Path()
            path.moveTo(x, y)
            path.lineTo(x, y)
            builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            val gesture = builder.build()
            accessibilityService.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {

                override fun onCancelled(gestureDescription: GestureDescription) {
                    super.onCancelled(gestureDescription)
                }

                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                }
            }, null)
    }
}