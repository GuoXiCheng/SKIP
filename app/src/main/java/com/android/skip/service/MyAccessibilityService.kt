package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.android.skip.AnalyticsManager
import com.android.skip.MyUtils

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        val rect = Rect()
        val rootNode = rootInActiveWindow
        if (rootNode != null) {

            if (!AnalyticsManager.isPerformScan(rootNode.packageName.toString())) return

            val skipNodes = MyUtils.handleRootNodeByPackageName(rootNode)
            if (skipNodes.isNotEmpty()) {
                skipNodes[0].getBoundsInScreen(rect)
                click(this, rect.exactCenterX(), rect.exactCenterY())
            }

            AnalyticsManager.increaseScanCount()
        }
    }

    override fun onInterrupt() {}

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