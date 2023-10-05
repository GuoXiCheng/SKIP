package com.android.skip.node

import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

class NodeRect: NodeCallBack {

    private val rect = Rect()
    private val list = mutableListOf<AccessibilityNodeInfo>()
    override fun onCallback(accessibilityNodeInfo: AccessibilityNodeInfo) {
        accessibilityNodeInfo.getBoundsInScreen(rect)
        if (rect.exactCenterX() > 800 && rect.exactCenterY() < 200) {
            list.clear()
            list.add(accessibilityNodeInfo)
        }

    }

    fun getList(): MutableList<AccessibilityNodeInfo> {
        return list
    }
}