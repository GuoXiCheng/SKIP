package com.android.skip.dataclass

import android.graphics.Rect

data class LoadSkipText(
    val text: String,
    val activityName: String? = null,
    val length: Int? = null,
    val click: Rect? = null
)

data class LoadSkipId(
    val id: String,
    val activityName: String? = null,
    val click: Rect? = null
)

data class LoadSkipBound(
    val bound: Rect,
    val activityName: String? = null,
    val click: Rect? = null
)

data class ConfigLoadSchema(
    val packageName: String,
    var skipTexts: List<LoadSkipText>? = null,
    val skipIds: List<LoadSkipId>? = null,
    val skipBounds: List<LoadSkipBound>? = null
)
