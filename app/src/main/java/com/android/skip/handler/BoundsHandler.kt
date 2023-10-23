package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.LogManager
import com.android.skip.manager.SkipConfigManager

class BoundsHandler: AbstractHandler() {

    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val skipRectList = SkipConfigManager.getSkipRectList(node.packageName.toString())
        return if (skipRectList != null) {
            val rect = traverseNode(node, skipRectList)
            if (rect != null) listOf(rect) else super.handle(node)
        } else {
            super.handle(node)
        }
    }

    private fun traverseNode(node: AccessibilityNodeInfo?, rectList: MutableList<Rect>): Rect? {
        node?.let {
            val rect = Rect().also { node.getBoundsInScreen(it) }
            rectList.firstOrNull { it.contains(rect) }?.let { return it }

            for (i in 0 until node.childCount) {
                val result = traverseNode(node.getChild(i), rectList)
                if (result != null) {
                    LogManager.i(result.toShortString())
                    return result
                }
            }
        }
        return null
    }

}