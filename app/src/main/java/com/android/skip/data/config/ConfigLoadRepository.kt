package com.android.skip.data.config

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.dataclass.ConfigLoadSchema
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigLoadRepository @Inject constructor() {
    private lateinit var configLoadSchemaMap: Map<String, ConfigLoadSchema>

    fun loadConfig(config: Map<String, ConfigLoadSchema>) {
        configLoadSchemaMap = config
    }

    suspend fun getTargetRect(rootNode: AccessibilityNodeInfo) =
        coroutineScope {
            val targetConfig = configLoadSchemaMap[rootNode.packageName]

            select {
                targetConfig?.skipTexts?.forEach { skipText ->
                    async {
                        rootNode.findAccessibilityNodeInfosByText(skipText.text).firstOrNull()
                    }.onAwait {
                        if (it != null) {
                            if (skipText.click != null) {
                                skipText.click
                            } else {
                                val rect = Rect()
                                it.getBoundsInScreen(rect)
                                rect
                            }
                        } else {
                            null
                        }
                    }
                }
                targetConfig?.skipIds?.forEach{ skipId ->
                    async {
                        rootNode.findAccessibilityNodeInfosByViewId(skipId.id).firstOrNull()
                    }.onAwait {
                        if (it != null) {
                            if (skipId.click != null) {
                                skipId.click
                            } else {
                                val rect = Rect()
                                it.getBoundsInScreen(rect)
                                rect
                            }
                        } else {
                            null
                        }
                    }
                }
            }
        }
}