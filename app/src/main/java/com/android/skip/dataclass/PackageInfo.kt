package com.android.skip.dataclass

import android.graphics.Rect

data class PackageInfo(
    val package_name: String, val skip_text: String?,
    val skip_id: String?, val start_page_node: Int?,
    val skip_points: List<String>?, var skip_rect_list: MutableList<Rect>,
    val skip_activity_name: String?
)
