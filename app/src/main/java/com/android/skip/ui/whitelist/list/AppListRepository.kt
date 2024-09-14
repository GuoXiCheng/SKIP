package com.android.skip.ui.whitelist.list

import com.android.skip.dataclass.AppListItem
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppListRepository @Inject constructor() {
    private var appInfos: MutableList<AppUtils.AppInfo> = mutableListOf()

    fun getData(currentPage: Int, pageSize: Int, isShowSystem: Boolean): List<AppListItem> {
        try {
            if (appInfos.isEmpty()) appInfos = AppUtils.getAppsInfo()

            val apps = if (isShowSystem) {
                appInfos
            } else {
                appInfos.filterNot { it.isSystem }
            }
            LogUtils.d(isShowSystem)
            val fromIndex = currentPage * pageSize
            val toIndex = minOf(fromIndex + pageSize, apps.size)

            return apps.subList(fromIndex, toIndex)
                .map { AppListItem(it.name, it.packageName, it.icon) }
        } catch (e: Exception) {
            return listOf()
        }
    }
}