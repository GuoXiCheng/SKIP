package com.android.skip.dataclass

data class NodeRootSchema(
    val fileId: Long,
    val appName: String,
    val packageName: String,
    val activityName: String,
    val screenHeight: Int,
    val screenWidth: Int,
    val nodes: MutableList<NodeChildSchema>
)