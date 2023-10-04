package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

class TextNodeHandler: AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {

        return node.findAccessibilityNodeInfosByText("跳过").map {
            val rect = Rect()
            it.getBoundsInScreen(rect)
            rect
        }
    }
}