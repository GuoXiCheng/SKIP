package com.android.skip.ui.whitelist.list

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.android.skip.MyApp
import com.android.skip.dataclass.AppListItem
import com.android.skip.ui.whitelist.WhiteListRepository
import javax.inject.Inject

class AppListRepository @Inject constructor() {

    @Inject
    lateinit var whiteListRepository: WhiteListRepository

    private val appInfos: MutableList<ApplicationInfo> by lazy {
        MyApp.context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    }

    fun getData(currentPage: Int, pageSize: Int, isShowSystem: Boolean, query: String = ""): List<AppListItem> {
        try {
            val pkgManager = MyApp.context.packageManager

            // 是否显示系统应用的过滤
            val apps = if (isShowSystem) {
                appInfos
            } else {
                appInfos.filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            }

            // 根据关键字进行过滤
            val filteredApps = if (query.isNotEmpty()) {
                apps.filter { it.loadLabel(pkgManager).toString().contains(query, ignoreCase = true) }
            } else {
                apps
            }

            val fromIndex = currentPage * pageSize
            val toIndex = minOf(fromIndex + pageSize, filteredApps.size)

            return filteredApps.subList(fromIndex, toIndex)
                .map {
                    AppListItem(
                        it.loadLabel(pkgManager).toString(),
                        it.packageName,
                        it.loadIcon(pkgManager),
                        whiteListRepository.isAppInWhiteList(it.packageName)
                    )
                }
        } catch (e: Exception) {
            return listOf()
        }
    }
}