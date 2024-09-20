package com.android.skip.dataclass

data class ReadSkipText(
    val text: String,
    val length: Int? = null,
    val activityName: String? = null,
    val click: String? = null
)

data class ReadSkipId(
    val id: String,
    val activityName: String? = null,
    val click: String? = null
)

data class ReadSkipBound(
    val bound: String,
    val activityName: String? = null,
    val click: String? = null
)

data class ConfigReadSchema(
    val packageName: String,
    val skipTexts: List<ReadSkipText>? = null,
    val skipIds: List<ReadSkipId>? = null,
    val skipBounds: List<ReadSkipBound>? = null
)
