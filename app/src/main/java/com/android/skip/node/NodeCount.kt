package com.android.skip.node

import android.view.accessibility.AccessibilityNodeInfo

class NodeCount: NodeCallBack {
    private var nodeCount = 0
    override fun onCallback(accessibilityNodeInfo: AccessibilityNodeInfo) {
        nodeCount ++
    }

    fun cleanCount() {
        nodeCount = 0
    }

    fun getCount(): Int {
        return nodeCount
    }
}