package com.android.skip.manager

import android.content.Context

object RectManager {
    var maxRectX = 0
    var maxRectY = 0

    fun setMaxRect(context: Context) {
        val metrics = context.resources.displayMetrics
        maxRectX = metrics.widthPixels
        maxRectY = metrics.heightPixels
    }

    fun getMaxRect(): String {
        return "${maxRectX}x${maxRectY}"
    }
}
