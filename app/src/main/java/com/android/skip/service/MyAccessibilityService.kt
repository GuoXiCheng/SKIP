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
import com.android.skip.node.NodeRect
import com.android.skip.node.recursionNodes

class MyAccessibilityService : AccessibilityService() {

    private val dataMap = mapOf(
        "com.example.pptv" to 25
    )

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        try {
            if (!AnalyticsManager.isPerformScan(getCurrentRootNode().packageName.toString())) return

            val textNodeHandler = TextNodeHandler()
            val idNodeHandler = IdNodeHandler()
            val pointHandler = PointHandler()

            textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(pointHandler)

            val listOfRect = textNodeHandler.handle(getCurrentRootNode())
            Log.i("SKIPS", listOfRect.toString())
            for (rect in listOfRect) {
                if (isStartUpPage()) {
                    click(this, rect)
                }
            }
//            val skipNodes = handleRootNodeByPackageName()
//            if (skipNodes.isNotEmpty()) {
//                skipNodes[0].getBoundsInScreen(RectManager.getRect())
//                click(this, RectManager.getRect())
//            }
//
//            if (getCurrentRootNode().packageName == "com.coolapk.market") {
//                click(this, RectManager.getRect(0.9f, 0.07f))
//                Log.i("SKIPS", "test coolapk: ${RectManager.getRect(0.9f, 0.07f)}")
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

    private fun handleRootNodeByPackageName (): MutableList<AccessibilityNodeInfo> {
        if (!isStartUpPage()) return mutableListOf()
        return when (getCurrentRootNode().packageName.toString()) {
            "com.qiyi.video.lite", "com.qiyi.video" -> getCurrentRootNode().findAccessibilityNodeInfosByText("关闭")
            else -> getNeedsClickNode()
        }
    }

    private fun isStartUpPage(): Boolean {
        val countCallBack = NodeCount()
        countCallBack.cleanCount()
        val currentNode = getCurrentRootNode()
        recursionNodes(currentNode, countCallBack)
        Log.i("SKIPS", "${currentNode.packageName}: ${countCallBack.getCount()}")
        return countCallBack.getCount() < SkipConfigManager.getStartPageNodeCount(currentNode.packageName.toString())
    }

    private fun getNeedsClickNode(): MutableList<AccessibilityNodeInfo> {
        val nodes = getCurrentRootNode().findAccessibilityNodeInfosByText("跳过")
        return if (nodes.isEmpty()) {
            val nodeRectCallBack = NodeRect()
            recursionNodes(getCurrentRootNode(), nodeRectCallBack)
            nodeRectCallBack.getList()
        } else {
            nodes
        }
    }

    override fun onInterrupt() {}

}