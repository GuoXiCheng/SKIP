package com.android.skip.dataclass

import android.graphics.Rect

data class PackageInfo(
    val package_name: String, val skip_text: String?,
    val skip_id: String?, var skip_rect_list: MutableList<Rect>,
    val skip_bounds: List<String>?, val max_click_count: Int?,
    val bypass: List<String>?
)
