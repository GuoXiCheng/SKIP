package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.MyUtils.click
import com.android.skip.handler.IdNodeHandler
import com.android.skip.handler.PointHandler
import com.android.skip.handler.TextNodeHandler
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.NodeManager
import com.android.skip.manager.SkipConfigManager
import com.android.skip.node.NodeCount
import com.android.skip.node.recursionNodes


class MyAccessibilityService : AccessibilityService() {
    private val countCallBack = NodeCount()

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {

            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val rootNode = getCurrentRootNode()
            if (!NodeManager.isGreaterThanConfig(
                    rootNode,
                    SkipConfigManager.getStartPageNodeCount(rootNode.packageName.toString())
                )
            ) {
                val textNodeHandler = TextNodeHandler()
                val idNodeHandler = IdNodeHandler()
                val pointHandler = PointHandler()
                textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(pointHandler)
                val listOfRect = textNodeHandler.handle(getCurrentRootNode())
                for (rect in listOfRect) {
                    click(this, rect)
                }
            }

//            if (isStartUpPage(getCurrentRootNode(), p0?.className.toString())) {
//                for (rect in listOfRect) {
//                    click(this, rect)
//                }
//            }

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

//    private fun isStartUpPage(
//        currentNode: AccessibilityNodeInfo,
//        currentActivityName: String
//    ): Boolean {
//        val startPageNodeCount =
//            SkipConfigManager.getStartPageNodeCount(currentNode.packageName.toString())
//        if (startPageNodeCount != null) {
//            countCallBack.cleanCount()
//            recursionNodes(currentNode, countCallBack)
//            return countCallBack.getCount() < startPageNodeCount
//        }

//        val startPageActivityName =
//            SkipConfigManager.getStartPageActivityName(currentNode.packageName.toString())
//        if (startPageActivityName != null) {
//            return startPageActivityName == currentActivityName
//        }
//        return true
//    }

    override fun onInterrupt() {}

}