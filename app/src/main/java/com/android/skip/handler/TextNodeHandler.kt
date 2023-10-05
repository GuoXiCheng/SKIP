package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class TextNodeHandler : AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val listOfRect = node.findAccessibilityNodeInfosByText(
            SkipConfigManager.getSkipText(node.packageName.toString())
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