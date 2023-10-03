package com.android.skip.node

import android.view.accessibility.AccessibilityNodeInfo

interface NodeCallBack {
    fun onCallback(accessibilityNodeInfo: AccessibilityNodeInfo)
}

fun recursionNodes(node: AccessibilityNodeInfo, callback: NodeCallBack) {
    callback.onCallback(node)
    for (i in 0 until node.childCount) {
        val childNode = node.getChild(i)
        if (childNode != null) {
            recursionNodes(childNode, callback)
            childNode.recycle()
        }
    }
}