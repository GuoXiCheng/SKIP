package com.android.oneclick.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService: AccessibilityService() {

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        val rect = Rect()
        val windowNodes = windows
        val rootNode = rootInActiveWindow
        for (i in 0 until windowNodes.size) {
            val rootWindow = windowNodes[i].root
            if (rootWindow != null && rootNode == rootWindow) {
//                Log.i("MyAccessibility", rootWindow.packageName.toString())
//                Log.i("MyAccessibility", rootWindow.packageName.toString())
                val position = getNodeText(rootWindow, "跳过", rect)
                if (position.toShortString() != "[0,0][0,0]") {
                    click(this, rect.exactCenterX(), rect.exactCenterY())
                }
            }
        }
//        Log.i("MyAccessibilityService", windows.size.toString())
//        val rootWindow = rootInActiveWindow
//        if (rootWindow != null) {
//            val rect = Rect()
//            val position = getNodeText(rootInActiveWindow, "跳过", rect)
//            if (position.toShortString() != "[0,0][0,0]") {
//                click(this, rect.exactCenterX(), rect.exactCenterY())
//            }
//        }
    }
    override fun onInterrupt() {
        Log.i("AccessibilityService", "onInterrupt")
    }

    private fun getNodeText(accessibilityNodeInfo: AccessibilityNodeInfo, nodeText: String, rect: Rect): Rect {
        val childCounts = accessibilityNodeInfo.childCount
        if (childCounts == 0) {
            if (accessibilityNodeInfo.text != null) {
//                Log.i("MyAccessibilityService", accessibilityNodeInfo.text.toString())
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
                    Log.i("click", "onCancel")
                }

                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
//                    Log.i("click", "click: ($x, $y)")
//                    Toast.makeText(accessibilityService, "已为您跳过广告", Toast.LENGTH_SHORT).show()
                }
            }, null)
    }
}