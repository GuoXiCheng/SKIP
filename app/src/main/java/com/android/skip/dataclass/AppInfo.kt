package com.android.skip.dataclass

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable
)
