package com.android.skip.utils

import com.blankj.utilcode.util.AppUtils

object InstalledAppUtils {
    private var cachedAppsList: MutableList<AppUtils.AppInfo> = mutableListOf()
    private var cachedAppsMap: MutableMap<String, AppUtils.AppInfo> = mutableMapOf()

    fun updateInstalledAppsCache() {
        val appInfo = AppUtils.getAppsInfo()
        cachedAppsList = appInfo
    }

    fun getCachedApps(includeSystemApp: Boolean = false): List<AppUtils.AppInfo> {
        return if (includeSystemApp) cachedAppsList
        else cachedAppsList.filterNot { it.isSystem }
    }

    fun getAppInfoByPackageName(packageName: String): AppUtils.AppInfo {
        return cachedAppsMap.getOrPut(packageName) {
            cachedAppsList.firstOrNull { it.packageName == packageName }
                ?: throw NoSuchElementException("No app found with package name: $packageName")
        }
    }
}