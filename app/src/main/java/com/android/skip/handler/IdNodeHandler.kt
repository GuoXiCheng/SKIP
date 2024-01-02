package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManagerV2

class IdNodeHandler : AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipIds = SkipConfigManagerV2.getSkipIds(node.packageName.toString())
        if (skipIds.isEmpty()) return super.handle(node)

        val listOfRect: MutableList<Rect> = mutableListOf()
        for (skipId in skipIds) {
            val targetNode =
                node.findAccessibilityNodeInfosByViewId(skipId.id).firstOrNull() ?: continue

            val rect = Rect()
            targetNode.getBoundsInScreen(rect)
            listOfRect.add(rect)
        }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}