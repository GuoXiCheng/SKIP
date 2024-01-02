package com.android.skip.dataclass

import android.graphics.Rect

data class PackageInfoV2(
    val packageName: String,
    val skipIds: List<SkipId>? = null,
    val skipTexts: List<SkipText>? = null,
    val skipBounds: List<SkipBound>? = null
)

data class SkipId(
    val id: String
)

data class SkipText(
    val text: String,
    val length: Int? = null
)

data class SkipBound(
    val bound: String,
    val resolution: String,
    var rect: Rect?
)