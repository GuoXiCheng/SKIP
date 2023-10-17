package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.handler.BoundsHandler
import com.android.skip.handler.IdNodeHandler
import com.android.skip.handler.TextNodeHandler
import com.android.skip.manager.*


class MyAccessibilityService : AccessibilityService() {
    private var clickCount = 0
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {

            if (p0 != null) {
                if (p0.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    clickCount = 0
                }
            }

            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val textNodeHandler = TextNodeHandler()
            val idNodeHandler = IdNodeHandler()
            val boundsHandler = BoundsHandler()
            textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(boundsHandler)
            val listOfRect = textNodeHandler.handle(getCurrentRootNode())
            for (rect in listOfRect) {
                if (clickCount < 1) {
                    LogManager.i(clickCount.toString())
                    click(this, rect)
                    clickCount += 1
                }
            }


        } catch (e: Exception) {

        } finally {
            AnalyticsManager.increaseScanCount()
        }
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        val rootNode = rootInActiveWindow
        if (rootNode != null) return rootNode
        else throw IllegalStateException("No valid root node available");
    }

    override fun onInterrupt() {}

    private fun click(accessibilityService: AccessibilityService, rect: Rect) {
        val path = Path()
        path.reset()
        path.moveTo(rect.exactCenterX(), rect.exactCenterY())
        path.lineTo(rect.exactCenterX(), rect.exactCenterY())

        val builder = GestureDescription.Builder()
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()

        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)

                    if (AnalyticsManager.isShowToast()) {
                        ToastManager.showToast(accessibilityService, "已为您跳过广告")
                        AnalyticsManager.setShowToastCount()
                    }

                }
            },
            null
        )
    }

}