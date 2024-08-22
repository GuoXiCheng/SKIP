package com.android.skip.dataclass

data class NodeRootSchema(
    val packageName: String,
    val className: String,
    val screenHeight: Int,
    val screenWidth: Int,
    val nodes: MutableList<NodeChildSchema>
)
