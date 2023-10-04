package com.android.skip.manager

import android.content.Context
import android.graphics.Rect

object RectManager {
    private var maxRectX = 1080
    private var maxRectY = 2268
    private val rect = Rect()

    fun setMaxRect(context: Context) {
        val metrics = context.resources.displayMetrics
        maxRectX = metrics.widthPixels
        maxRectY = metrics.heightPixels
    }

    fun getRect(): Rect {
        return rect
    }

    fun getRect(centerX: Int, centerY: Int): Rect {
        rect.set(centerX, centerY, maxRectX.minus(centerX), maxRectY.minus(centerY))
        return rect
    }

    fun getRect(percentX: Float, percentY: Float): Rect {
        val actualX = (percentX * maxRectX).toInt()
        val actualY = (percentY * maxRectY).toInt()
        rect.set(actualX, actualY, maxRectX.minus(actualX), maxRectY.minus(actualY))
        return rect
    }
}
