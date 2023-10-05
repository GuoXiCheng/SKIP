package com.android.skip.handler

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

abstract class AbstractHandler: NodeHandler {
    private var nextHandler: NodeHandler? = null

    //    override fun handle(rectList: MutableList<Rect>): MutableList<Rect> {
//        if (nextHandler != null && nextHandler is NodeHandler) {
//            val nodeHandler = nextHandler as NodeHandler
//            return nodeHandler.handle(rectList)
//        }
//        return rectList
//    }
//
    override fun handle(node: AccessibilityNodeInfo): List<Rect> {
        if (nextHandler != null) {
            return nextHandler!!.handle(node)
        }
        return listOf()
    }

    override fun setNextHandler(handler: NodeHandler): NodeHandler {
        nextHandler = handler
        return handler
    }
}