package com.android.skip.dataclass

data class PackageInfo(
    val package_name: String, val skip_text: String?,
    val skip_id: String?, val start_page_node: Int?,
    val skip_point: String?
)
