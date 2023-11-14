package com.android.skip.manager

import android.graphics.Rect
import com.android.skip.dataclass.PackageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SkipConfigManager {
    private lateinit var appInfoMap: Map<String, PackageInfo>

    fun setConfig(config: Any) {
        val gson = Gson()
        val json = gson.toJson(config)
        val appInfoList: List<PackageInfo> =
            gson.fromJson(json, object : TypeToken<List<PackageInfo>>() {}.type)
        val newAppInfoList = appInfoList.map { packageInfo ->
            packageInfo.skip_rect_list = parseSkipBounds(packageInfo)
            packageInfo
        }
        appInfoMap = newAppInfoList.associateBy { it.package_name }
    }

    private fun parseSkipBounds(packageInfo: PackageInfo): MutableList<Rect> {
        val skipBounds = packageInfo.skip_bounds
        val skipRectList = mutableListOf<Rect>()

        if (skipBounds is List && skipBounds.isNotEmpty()) {
            for (bounds in skipBounds) {
                val boundsParts = bounds.split("#")
                if (boundsParts.size != 2) continue

                val (maxXYParts, detailBoundsParts) = boundsParts.map { it.split(",") }
                if (maxXYParts.size != 2 || detailBoundsParts.size != 4) continue

                val (maxX, maxY) = maxXYParts
                val (boundsLeft, boundsTop, boundsRight, boundsBottom) = detailBoundsParts

                skipRectList.add(
                    Rect(
                        (boundsLeft.toInt() * RectManager.maxRectX / maxX.toInt() - 1),
                        (boundsTop.toInt() * RectManager.maxRectY / maxY.toInt() - 1),
                        (boundsRight.toInt() * RectManager.maxRectX / maxX.toInt() + 1),
                        (boundsBottom.toInt() * RectManager.maxRectY / maxY.toInt() + 1)
                    )
                )
            }
        }
        return skipRectList
    }

    fun getSkipText(packageName: String): String {
        return appInfoMap[packageName]?.skip_text ?: "跳过"
    }

    fun getSkipId(packageName: String): String? {
        return appInfoMap[packageName]?.skip_id
    }

    fun getSkipRectList(packageName: String): MutableList<Rect>? {
        return appInfoMap[packageName]?.skip_rect_list
    }

    fun getMaxClickCount(packageName: String): Int? {
        return appInfoMap[packageName]?.max_click_count
    }

    fun getBypass(packageName: String): List<String> {
        return appInfoMap[packageName]?.bypass?: emptyList()
    }
}