package com.android.skip.handler

import android.graphics.Rect

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
    override fun setNextHandler(handler: NodeHandler): NodeHandler {
        nextHandler = handler
        return handler
    }
}