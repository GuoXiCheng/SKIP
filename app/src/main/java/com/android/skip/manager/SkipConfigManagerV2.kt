package com.android.skip.manager

import android.graphics.Rect
import com.android.skip.dataclass.PackageInfoV2
import com.android.skip.dataclass.SkipBound
import com.android.skip.dataclass.SkipId
import com.android.skip.dataclass.SkipText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SkipConfigManagerV2 {
    private lateinit var packageInfoMap: Map<String, PackageInfoV2>

    fun setConfig(config: Any) {
        val gson = Gson()
        val json = gson.toJson(config)
        val rawList: List<PackageInfoV2> =
            gson.fromJson(json, object : TypeToken<List<PackageInfoV2>>() {}.type)

        packageInfoMap = parseSkipBounds(rawList).associateBy { it.packageName }
    }

    private fun parseSkipBounds(rawList: List<PackageInfoV2>): List<PackageInfoV2> {
        val maxX = RectManager.maxRectX
        val maxY = RectManager.maxRectY

        for (raw in rawList) {
            raw.skipBounds?.forEach { skipBound ->
                val (boundLeft, boundTop, boundRight, boundBottom) = skipBound.bound.split(",")
                    .map { it.toInt() }
                val (resolutionX, resolutionY) = skipBound.resolution.split(",").map { it.toInt() }

                skipBound.rect = Rect(
                    boundLeft * maxX / resolutionX - 1,
                    boundTop * maxY / resolutionY - 1,
                    boundRight * maxX / resolutionX + 1,
                    boundBottom * maxY / resolutionY + 1
                )
            }
        }
        return rawList
    }

    fun getSkipIds(packageName: String): List<SkipId> {
        return packageInfoMap[packageName]?.skipIds ?: emptyList()
    }

    fun getSkipTexts(packageName: String): List<SkipText> {
        return packageInfoMap[packageName]?.skipTexts ?: listOf(SkipText(text = "跳过"))
    }

    fun getSkipBounds(packageName: String): List<SkipBound> {
        return packageInfoMap[packageName]?.skipBounds ?: emptyList()
    }
}