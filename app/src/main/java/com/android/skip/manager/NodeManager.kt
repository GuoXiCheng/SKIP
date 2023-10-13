package com.android.skip.manager
import android.view.accessibility.AccessibilityNodeInfo

object NodeManager {
    private var nodeCount = 0
    private var nodeThreshold = 0

    fun isGreaterThanConfig(rootNode: AccessibilityNodeInfo, skipNodeCount: Int?): Boolean {
        nodeCount = 0
        if (skipNodeCount is Int) {
            nodeThreshold = skipNodeCount
        } else {
            LogManager.i("${rootNode.packageName} skip_node_count is null return: false")
            return false
        }
        val result = countNodes(rootNode)
        LogManager.i("${rootNode.packageName} node count is $nodeCount and is greater than config: $result")
        return result
    }

    private fun countNodes(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        nodeCount++
        if (nodeCount > nodeThreshold) {
            return true
        }

        for (i in 0 until node.childCount) {
            var childNode: AccessibilityNodeInfo? = null

            try {
                childNode = node.getChild(i)
                if (childNode != null && countNodes(childNode)) {
                    return true
                }
            } catch (e: IllegalStateException) {
                // Ignore and continue with the next child
            } finally {
                childNode?.recycle() // Recycle the child node
            }
        }

        return false
    }
}