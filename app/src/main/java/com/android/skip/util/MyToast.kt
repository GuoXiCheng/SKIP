package com.android.skip.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.android.skip.MyApp

object MyToast {
    fun show(content: String) {
        Handler(Looper.getMainLooper()).post{
            Toast.makeText(MyApp.context, content, Toast.LENGTH_SHORT).show()
        }
    }
}