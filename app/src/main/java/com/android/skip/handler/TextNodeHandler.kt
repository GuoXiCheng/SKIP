package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManagerV2

class TextNodeHandler : AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipTexts = SkipConfigManagerV2.getSkipTexts(node.packageName.toString())
        if (skipTexts.isEmpty()) return super.handle(node)

        val listOfRect: MutableList<Rect> = mutableListOf()

        for (skipText in skipTexts) {
            val targetNode =
                node.findAccessibilityNodeInfosByText(skipText.text).firstOrNull() ?: continue

            if (skipText.length != null && targetNode.text.length > skipText.length) {
                continue
            }

            val rect = Rect()
            targetNode.getBoundsInScreen(rect)
            listOfRect.add(rect)
        }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}