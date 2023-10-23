package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class IdNodeHandler: AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipId = SkipConfigManager.getSkipId(node.packageName.toString()) ?: return super.handle(node)

        val listOfRect = node.findAccessibilityNodeInfosByViewId(skipId).map {
            val rect = Rect()
            it.getBoundsInScreen(rect)
            rect
        }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}