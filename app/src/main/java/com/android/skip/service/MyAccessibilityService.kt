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
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.ToastManager
import com.android.skip.manager.WhitelistManager


class MyAccessibilityService : AccessibilityService() {
    private val textNodeHandler = TextNodeHandler()
    private val idNodeHandler = IdNodeHandler()
    private val boundsHandler = BoundsHandler()

    init {
        textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(boundsHandler)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            event ?: return

            val rootNode = getCurrentRootNode()

            if (!AnalyticsManager.isPerformScan(rootNode.packageName.toString())) return

            if (WhitelistManager.isInWhitelist(rootNode.packageName.toString())) return

            val listOfRect = textNodeHandler.handle(rootNode)
            for (rect in listOfRect) {
                click(this, rect)
            }
        } catch (e: Exception) {
            // Log the exception or handle it in some other way
        } finally {
            AnalyticsManager.increaseScanCount()
        }
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        return rootInActiveWindow ?: throw IllegalStateException("No valid root node available")
    }

    override fun onInterrupt() {}

    private fun click(accessibilityService: AccessibilityService, rect: Rect) {
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        path.lineTo(rect.centerX().toFloat(), rect.centerY().toFloat())

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            .build()

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