package com.android.skip.dataclass

data class ReadClick(
    val position: String,
    val resolution: String
)

data class ReadSkipText(
    val text: String,
    val length: Int? = null,
    val activityName: String? = null,
    val click: ReadClick? = null
)

data class ReadSkipId(
    val id: String,
    val activityName: String? = null,
    val click: ReadClick? = null
)

data class ReadSkipBound(
    val bound: String,
    val resolution: String,
    val activityName: String? = null,
    val click: ReadClick? = null
)

data class ConfigReadSchema(
    val packageName: String,
    val skipTexts: List<ReadSkipText>? = null,
    val skipIds: List<ReadSkipId>? = null,
    val skipBounds: List<ReadSkipBound>? = null
)
