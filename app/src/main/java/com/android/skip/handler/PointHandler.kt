package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class PointHandler: AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipRectList = SkipConfigManager.getSkipRectList(node.packageName.toString())
        return if (skipRectList.isEmpty()) {
            super.handle(node)
        } else {
            skipRectList
        }
    }
}