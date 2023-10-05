package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class IdNodeHandler: AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val listOfRect = node.findAccessibilityNodeInfosByViewId(
            SkipConfigManager.getSkipId(node.packageName.toString())
        ).map {
            val rect = Rect()
            it.getBoundsInScreen(rect)
            rect
        }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}