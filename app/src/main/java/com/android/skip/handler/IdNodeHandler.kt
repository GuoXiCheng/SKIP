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
//        val skipId = SkipConfigManager.getSkipId(node.packageName.toString()) ?: return super.handle(node)
//
//        val nodes = findAccessibilityNodeInfosContainsViewId(node, skipId)
//        val listOfRect = nodes.map {
//            val rect = Rect()
//            it.getBoundsInScreen(rect)
//            rect
//        }
//        nodes.forEach { it.recycle() }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }

    private fun findAccessibilityNodeInfosContainsViewId(
        node: AccessibilityNodeInfo,
        viewId: String
    ): MutableList<AccessibilityNodeInfo> {
        val resultList = mutableListOf<AccessibilityNodeInfo>()

        dfs(node, viewId, resultList)

        return resultList
    }

    private fun dfs(
        node: AccessibilityNodeInfo,
        viewId: String,
        resultList: MutableList<AccessibilityNodeInfo>
    ) {
        if (node.viewIdResourceName?.contains(viewId) == true) {
            resultList.add(AccessibilityNodeInfo.obtain(node))
        }

        for (i in 0 until node.childCount) {
            dfs(node.getChild(i), viewId, resultList)
        }
    }
}