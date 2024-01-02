package com.android.skip.manager

import android.content.Context
import android.util.Log
import com.android.skip.WHITELIST_DOT
import com.android.skip.utils.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object WhitelistManager {
    private val whitelist = mutableListOf<String>()

    fun setWhitelist(scope: CoroutineScope, context: Context) {
        whitelist.clear()
        scope.launch {
            try {
                val allPreferences = context.dataStore.data.first()
                for (entry in allPreferences.asMap().entries) {
                    val key = entry.key
                    val value = entry.value
                    if (key.name.startsWith(WHITELIST_DOT) && value == true) {
                        whitelist.add(key.name.substringAfter(WHITELIST_DOT))
                    }
                }
            } catch (e: Exception) {
                // 处理可能的异常
                Log.e("DataStoreError", "Error reading preferences", e)
            }
        }
    }

    fun isInWhitelist(packageName: String): Boolean {
        return whitelist.indexOf(packageName) != -1
    }
}