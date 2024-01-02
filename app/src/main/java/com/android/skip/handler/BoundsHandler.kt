package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManagerV2

class BoundsHandler: AbstractHandler() {

    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipBounds = SkipConfigManagerV2.getSkipBounds(node.packageName.toString())
        if (skipBounds.isEmpty()) return super.handle(node)

        val listOfRect: MutableList<Rect> = mutableListOf()
        for (skipBound in skipBounds) {
            if (skipBound.rect == null) continue
            val rect = traverseNode(node, skipBound.rect!!)
            if (rect != null) listOfRect.add(rect)
        }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }

    private fun traverseNode(node: AccessibilityNodeInfo?, targetRect: Rect): Rect? {
        node?.let {
            val rect = Rect().also { node.getBoundsInScreen(it) }
            if (targetRect.contains(rect)) return rect

            for (i in 0 until node.childCount) {
                val result = traverseNode(node.getChild(i), targetRect)
                if (result != null) {
                    return result
                }
            }
        }
        return null
    }

}