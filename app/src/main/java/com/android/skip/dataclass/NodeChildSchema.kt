package com.android.skip.dataclass

data class NodeChildSchema(
    val depth: Int,
    val childCount: Int,
    val parentId: Int,
    val nodeId: Int,
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val isClickable: Boolean,
    var className: String? = null,
    var text: String? = null,
    var viewIdResourceName: String? = null
)
