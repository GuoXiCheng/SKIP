package com.android.skip.manager

import android.graphics.Rect
import com.android.skip.dataclass.PackageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SkipConfigManager {
    private lateinit var appInfoMap: Map<String, PackageInfo>
    fun setConfig(config: String) {
        val gson = Gson()
        val appInfoList: List<PackageInfo> =
            gson.fromJson(config, object : TypeToken<List<PackageInfo>>() {}.type)
        handleConfig(appInfoList)
    }

    fun setConfig(appInfoList: List<PackageInfo>) {
        handleConfig(appInfoList)
    }

    private fun handleConfig(appInfoList: List<PackageInfo>) {
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

//    private fun handleConfig(appInfoList: List<PackageInfo>) {
//        val newAppInfoList = appInfoList.map { it ->
//            if (it.skip_bounds is List && it.skip_bounds.isNotEmpty()) {
//                it.skip_rect_list = mutableListOf()
//                for (bounds in it.skip_bounds) {
//                    val boundsParts = bounds.split("#")
//                    if (boundsParts.size == 2) {
//                        val maxXYParts = boundsParts[0].split(",")
//                        val detailBoundsParts = boundsParts[1].split(",")
//                        if (maxXYParts.size == 2 && detailBoundsParts.size == 4) {
//                            val (maxX, maxY) = maxXYParts
//                            val (boundsLeft, boundsTop, boundsRight, boundsBottom) = detailBoundsParts
//                            it.skip_rect_list.add(
//                                Rect(
//                                    (boundsLeft.toInt() * RectManager.maxRectX / maxX.toInt() - 1),
//                                    (boundsTop.toInt() * RectManager.maxRectY / maxY.toInt() - 1),
//                                    (boundsRight.toInt() * RectManager.maxRectX / maxX.toInt() + 1),
//                                    (boundsBottom.toInt() * RectManager.maxRectY / maxY.toInt() + 1)
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//            it
//        }
//        appInfoMap = newAppInfoList.associateBy { it.package_name }
//    }

    fun getSkipText(packageName: String): String {
        return appInfoMap[packageName]?.skip_text ?: "跳过"
    }

    fun getSkipId(packageName: String): String {
        return appInfoMap[packageName]?.skip_id ?: "no skip id"
    }

    fun getSkipRectList(packageName: String): MutableList<Rect> {
        return appInfoMap[packageName]?.skip_rect_list ?: mutableListOf()
    }

    fun getMaxClickCount(packageName: String): Int? {
        return appInfoMap[packageName]?.max_click_count
    }
}