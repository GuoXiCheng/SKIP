package com.android.skip.manager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastManager {
    fun showToast(context: Context, content: String) {
        Handler(Looper.getMainLooper()).post{
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        }
    }
}