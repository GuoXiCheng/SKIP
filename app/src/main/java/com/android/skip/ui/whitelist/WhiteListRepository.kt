package com.android.skip.ui.whitelist

import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.util.dataStore
import com.blankj.utilcode.util.StringUtils.getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WhiteListRepository @Inject constructor() {
    private val _whiteList: MutableList<String> by lazy {
        runBlocking {
            initWhitelist().toMutableList()
        }
    }

    private suspend fun initWhitelist() = withContext(Dispatchers.IO) {
        val allPreferences = MyApp.context.dataStore.data.first()
        allPreferences.asMap()
            .filter {
                it.key.name.startsWith(getString(R.string.whitelist_dot)) && it.value == true
            }.keys.map {
                it.name.substringAfter(
                    getString(R.string.whitelist_dot)
                )
            }
    }

    fun updateWhiteList(packageName: String, checked: Boolean) {
        if (checked) {
            _whiteList.add(packageName)
        } else {
            _whiteList.remove(packageName)
        }
    }

    fun isAppInWhiteList(packageName: String): Boolean {
        return _whiteList.indexOf(packageName) != -1
    }
}