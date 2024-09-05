package com.android.skip.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.android.skip.MyApp

object MyToast {
    fun show(content: Int) {
        Handler(Looper.getMainLooper()).post{
            Toast.makeText(MyApp.context, MyApp.context.getString(content), Toast.LENGTH_SHORT).show()
        }
    }
}