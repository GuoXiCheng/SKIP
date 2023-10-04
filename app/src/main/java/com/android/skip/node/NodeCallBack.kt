package com.android.skip.node

import android.view.accessibility.AccessibilityNodeInfo

interface NodeCallBack {
    fun onCallback(accessibilityNodeInfo: AccessibilityNodeInfo)
}

fun recursionNodes(node: AccessibilityNodeInfo?, callback: NodeCallBack) {
    if (node == null) return
    callback.onCallback(node)
    for (i in 0 until node.childCount) {
        val childNode = node.getChild(i)
        recursionNodes(childNode, callback)
        childNode.recycle()
    }
}
