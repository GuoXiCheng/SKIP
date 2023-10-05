package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

interface NodeHandler {
    fun handle(node: AccessibilityNodeInfo): List<Rect>
    fun setNextHandler (handler: NodeHandler): NodeHandler
}