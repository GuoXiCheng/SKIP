package com.android.skip.manager

import android.util.Log

object LogManager {
    private const val TAG = "SKIPS"

    private const val DEBUG = false

    fun i(message: String) {
        if (DEBUG) {
            Log.i(TAG, message)
        }
    }
}