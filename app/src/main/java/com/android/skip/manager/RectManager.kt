package com.android.skip.manager

import android.content.Context
import android.graphics.Rect

object RectManager {
    private var maxRectX = 1080
    private var maxRectY = 2268

    fun setMaxRect(context: Context) {
        val metrics = context.resources.displayMetrics
        maxRectX = metrics.widthPixels
        maxRectY = metrics.heightPixels
    }

    fun getPointRect(percentX: Float, percentY: Float): Rect {
        val rect = Rect()
        val actualX = (percentX * maxRectX).toInt()
        val actualY = (percentY * maxRectY).toInt()
        rect.set(actualX - 10, actualY - 10, actualX + 10, actualY + 10)
        return rect
    }
}
