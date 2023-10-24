package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class TextNodeHandler : AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val nodes = node.findAccessibilityNodeInfosByText(
            SkipConfigManager.getSkipText(node.packageName.toString())
        )
        val listOfRect = nodes.map {
            val rect = Rect()
            it.getBoundsInScreen(rect)
            rect
        }
        nodes.forEach { it.recycle() }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}