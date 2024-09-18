package com.android.skip.ui.whitelist.list

import com.android.skip.dataclass.AppListItem
import com.android.skip.ui.whitelist.WhiteListRepository
import com.blankj.utilcode.util.AppUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppListRepository @Inject constructor() {

    @Inject
    lateinit var whiteListRepository: WhiteListRepository

    private var appInfos: MutableList<AppUtils.AppInfo> = mutableListOf()

    fun getData(currentPage: Int, pageSize: Int, isShowSystem: Boolean): List<AppListItem> {
        try {
            if (appInfos.isEmpty()) appInfos = AppUtils.getAppsInfo()

            val apps = if (isShowSystem) {
                appInfos
            } else {
                appInfos.filterNot { it.isSystem }
            }
            val fromIndex = currentPage * pageSize
            val toIndex = minOf(fromIndex + pageSize, apps.size)

            return apps.subList(fromIndex, toIndex)
                .map {
                    AppListItem(
                        it.name,
                        it.packageName,
                        it.icon,
                        whiteListRepository.isAppInWhiteList(it.packageName)
                    )
                }
        } catch (e: Exception) {
            return listOf()
        }
    }
}