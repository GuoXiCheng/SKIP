package com.android.skip.dataclass

import android.view.accessibility.AccessibilityNodeInfo

data class AccessibilityNodeInfoCarrier(
    val node: AccessibilityNodeInfo,
    val depth: Int,
    val parentId: Int,
    val nodeId: Int
)