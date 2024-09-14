package com.android.skip.dataclass

import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState

data class AppListItem(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable,
    val checked: Boolean
)
