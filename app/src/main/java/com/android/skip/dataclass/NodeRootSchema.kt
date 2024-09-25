package com.android.skip.dataclass

data class NodeRootSchema(
    val fileId: String,
    val appName: String,
    val packageName: String,
    val activityName: String,
    val screenHeight: Int,
    val screenWidth: Int,
    val createTime: Long,
    val deviceName: String,
    val nodes: MutableList<NodeChildSchema>
)