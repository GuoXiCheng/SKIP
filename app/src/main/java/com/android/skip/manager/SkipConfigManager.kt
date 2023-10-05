package com.android.skip.manager
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
}