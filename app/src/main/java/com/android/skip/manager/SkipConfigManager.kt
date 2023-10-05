package com.android.skip.manager
import android.graphics.Rect
import com.android.skip.dataclass.PackageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SkipConfigManager {
    private lateinit var appInfoMap: Map<String, PackageInfo>
    fun setConfig(config: String) {
        val gson = Gson()
        val appInfoList: List<PackageInfo> = gson.fromJson(config, object : TypeToken<List<PackageInfo>>() {}.type)
        appInfoMap = appInfoList.associateBy { it.package_name }
    }

    fun getSkipText(packageName: String): String {
        return appInfoMap[packageName]?.skip_text ?: "跳过"
    }

    fun getSkipId(packageName: String): String {
        return appInfoMap[packageName]?.skip_id ?: "no skip id"
    }

    fun getStartPageNodeCount(packageName: String): Int {
        return appInfoMap[packageName]?.start_page_node ?: 10
    }

    fun getSkipPoint(packageName: String): Rect? {
        val skipPoint = appInfoMap[packageName]?.skip_point
        val pointParts = skipPoint?.split(",")?.map { it.toFloatOrNull() }
        if (pointParts?.size == 2 && isBetweenZeroAndOne(pointParts[0]) && isBetweenZeroAndOne(pointParts[1])) {
            val (x, y) = pointParts
            if (x is Float && y is Float) {
                return RectManager.getPointRect(x, y)
            }
        }
        return null
    }

    private fun isBetweenZeroAndOne(value: Float?): Boolean {
        return value != null && value in 0.0f..1.0f
    }
}