package com.android.skip.manager

import com.android.skip.dataclass.PackageInfoV2
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
        packageInfoMap = rawList.associateBy { it.packageName }

    }

    fun getSkipIds(packageName: String): List<SkipId> {
        return packageInfoMap[packageName]?.skipIds ?: emptyList()
    }

    fun getSkipTexts(packageName: String): List<SkipText> {
        return packageInfoMap[packageName]?. skipTexts ?: listOf(SkipText(text="跳过"))
    }
}