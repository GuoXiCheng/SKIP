package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class PointHandler: AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipPoint = SkipConfigManager.getSkipPoint(node.packageName.toString())
        return if (skipPoint != null) {
            listOf(skipPoint)
        } else {
            super.handle(node)
        }
    }
}