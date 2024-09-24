package com.android.skip.dataclass

enum class VersionState {
    PENDING, CURRENT_LATEST, DISCOVER_LATEST
}

data class VersionPostSchema(
    val status: VersionState,
    val value: String,
    val latestVersion: String
)