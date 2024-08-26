package com.android.skip.dataclass

data class SkipIdsV3(val id: String)

data class SkipTextsV3(val text: String, val length: Int? = null)

data class SkipBoundsV3(val bound: String)

data class ConfigV3(
    val packageName: String,
    val skipIds: List<SkipIdsV3>? = null,
    val skipTexts: List<SkipTextsV3>? = null,
    val skipBounds: List<SkipBoundsV3>? = null
)

data class SkipConfigV3(val config: List<ConfigV3>)
