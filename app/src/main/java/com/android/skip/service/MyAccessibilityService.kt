package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.MyUtils.click
import com.android.skip.handler.BoundsHandler
import com.android.skip.handler.IdNodeHandler
import com.android.skip.handler.TextNodeHandler
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.NodeManager
import com.android.skip.manager.SkipConfigManager


class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {
            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val textNodeHandler = TextNodeHandler()
            val idNodeHandler = IdNodeHandler()
            val boundsHandler = BoundsHandler()
            textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(boundsHandler)
            val listOfRect = textNodeHandler.handle(getCurrentRootNode())
            for (rect in listOfRect) {
                val rootNode = getCurrentRootNode()
                if (!NodeManager.isGreaterThanConfig(
                        rootNode,
                        SkipConfigManager.getStartPageNodeCount(rootNode.packageName.toString())
                    )
                ) {
                    click(this, rect)
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

}