package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.LogManager
import com.android.skip.manager.SkipConfigManager

class BoundsHandler: AbstractHandler() {

    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val rect = traverseNode(node, SkipConfigManager.getSkipRectList(node.packageName.toString()))
        return if (rect != null) {
            listOf(rect)
        } else {
            super.handle(node)
        }
    }

    private fun traverseNode(node: AccessibilityNodeInfo?, rectList: MutableList<Rect>): Rect? {
        if (node == null) return null

        val rect = Rect()
        node.getBoundsInScreen(rect)

        for (rectItem in rectList) {
            if (rectItem.contains(rect)) {
                return rect
            }
        }

        // 遍历子节点
        for (i in 0 until node.childCount) {
            val result = traverseNode(node.getChild(i), rectList)
            if (result != null) {
                LogManager.i(result.toShortString())
                return result // 返回包含的矩形
            }
        }

        return null // 如果没有找到包含的矩形，返回 null
    }

}