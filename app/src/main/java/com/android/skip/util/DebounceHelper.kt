package com.android.skip.util

class DebounceHelper(private val delayMillis: Long) {
    private var lastTime: Long = 0

    fun run(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime >= delayMillis) {
            action()
            lastTime = currentTime
        }
    }
}