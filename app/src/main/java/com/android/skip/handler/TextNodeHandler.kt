package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.manager.SkipConfigManager

class TextNodeHandler : AbstractHandler() {
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        val nodes = node.findAccessibilityNodeInfosByText(
            SkipConfigManager.getSkipText(node.packageName.toString())
        )
        val bypass = SkipConfigManager.getBypass(node.packageName.toString())

        val listOfRect = ArrayList<Rect>()
        for(t in nodes){
            if(!bypass.contains(t.viewIdResourceName)){
                t.apply {
                    Rect().also { rect ->

                        getBoundsInScreen(rect)
                        listOfRect.add(rect)
                    }
                }
            }
        }
        nodes.forEach { it.recycle() }

        return listOfRect.ifEmpty {
            super.handle(node)
        }
    }
}