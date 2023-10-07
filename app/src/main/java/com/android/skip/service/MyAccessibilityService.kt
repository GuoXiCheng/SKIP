package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.MyUtils.click
import com.android.skip.handler.IdNodeHandler
import com.android.skip.handler.PointHandler
import com.android.skip.handler.TextNodeHandler
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.SkipConfigManager
import com.android.skip.node.NodeCount
import com.android.skip.node.recursionNodes


class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {
            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val textNodeHandler = TextNodeHandler()
            val idNodeHandler = IdNodeHandler()
            val pointHandler = PointHandler()

            textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(pointHandler)

            val listOfRect = textNodeHandler.handle(getCurrentRootNode())
            if (isStartUpPage()) {
                for (rect in listOfRect) {
                    click(this, rect)
                }
            }
//            for (rect in listOfRect) {
//                if (isStartUpPage()) {
//                    click(this, rect)
//                }
//            }

            AnalyticsManager.increaseScanCount()
        } catch (e: Exception) {
            Log.d("SKIPS", e.message.toString())
        }
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        val rootNode = rootInActiveWindow
        if (rootNode != null) return rootNode
        else throw IllegalStateException("No valid root node available");
    }

    private fun isStartUpPage(): Boolean {
        val countCallBack = NodeCount()
        countCallBack.cleanCount()
        val currentNode = getCurrentRootNode()
        recursionNodes(currentNode, countCallBack)
        return countCallBack.getCount() < SkipConfigManager.getStartPageNodeCount(currentNode.packageName.toString())
    }

    override fun onInterrupt() {}

}