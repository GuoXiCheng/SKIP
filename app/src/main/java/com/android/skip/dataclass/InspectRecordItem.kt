package com.android.skip.dataclass

import android.graphics.drawable.Drawable

data class InspectRecordItem(
    val fileId: String,
    val appIcon: Drawable,
    val appName: String,
    val packageName: String,
    val activityName: String,
    val createTime: Long
)
