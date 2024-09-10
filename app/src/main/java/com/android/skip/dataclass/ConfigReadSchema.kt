package com.android.skip.dataclass

data class ReadClick(val position: String, val resolution: String)

data class ReadSkipText(val text: String, val length: Int, val click: ReadClick?)

data class ReadSkipId(val id: String, val click: ReadClick?)

data class ReadSkipBound(val bound: String, val resolution: String, val click: ReadClick?)

data class ConfigReadSchema(
    val packageName: String,
    val skipTexts: List<ReadSkipText>?,
    val skipIds: List<ReadSkipId>?,
    val skipBounds: List<ReadSkipBound>?
)
