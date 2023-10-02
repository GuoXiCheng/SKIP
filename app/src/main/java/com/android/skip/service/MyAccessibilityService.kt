package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.android.skip.AnalyticsManager

class MyAccessibilityService : AccessibilityService() {

    private val path = Path()
    private val rect = Rect()

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {

        try {
            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val skipNodes = handleRootNodeByPackageName()
            if (skipNodes.isNotEmpty()) {
                skipNodes[0].getBoundsInScreen(rect)
                click(this, rect.exactCenterX(), rect.exactCenterY())
            }

            AnalyticsManager.increaseScanCount()
        } catch (e: Exception) {
            Log.i("SKIPS", e.message.toString())
        }
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        val rootNode = rootInActiveWindow
        if (rootNode != null) return rootNode
        else throw IllegalStateException("No valid root node available");
    }

    fun handleRootNodeByPackageName (): MutableList<AccessibilityNodeInfo> {
        return when (getCurrentRootNode().packageName.toString()) {
            "com.qiyi.video.lite", "com.qiyi.video" -> getCurrentRootNode().findAccessibilityNodeInfosByText("关闭")
            "com.MobileTicket" -> getCurrentRootNode().findAccessibilityNodeInfosByViewId("com.MobileTicket:id/tv_skip")
            "com.coolapk.market" -> getCurrentRootNode().findAccessibilityNodeInfosByViewId("com.coolapk.market:id/tt_splash_skip_btn")
            else -> getCurrentRootNode().findAccessibilityNodeInfosByText("跳过")
        }
    }

    override fun onInterrupt() {}

    private fun click(accessibilityService: AccessibilityService, x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        path.lineTo(x, y)

        val builder = GestureDescription.Builder()
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()

        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)

                    if (AnalyticsManager.isShowToast()) {
                        Toast.makeText(accessibilityService, "已为您跳过广告", Toast.LENGTH_SHORT).show()
                        AnalyticsManager.setShowToastCount()
                    }

                }
            },
            null
        )
    }

}