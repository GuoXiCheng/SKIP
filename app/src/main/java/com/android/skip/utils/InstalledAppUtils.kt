package com.android.skip.utils

import com.blankj.utilcode.util.AppUtils

object InstalledAppUtils {
    var cachedApps: MutableList<AppUtils.AppInfo> = mutableListOf()

    fun updateInstalledAppsCache() {
        val appInfo = AppUtils.getAppsInfo()
        cachedApps = appInfo
    }
}