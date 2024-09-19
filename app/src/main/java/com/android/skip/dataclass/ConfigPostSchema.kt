package com.android.skip.dataclass

enum class ConfigState {
    SUCCESS, PENDING, FAIL
}

data class ConfigPostSchema(
    val status: ConfigState,
    val value: String,
    val configReadSchemaList: List<ConfigReadSchema>? = null
)