package com.android.skip.data

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select
import javax.inject.Inject
import javax.inject.Singleton

data class Click(val position: String, val resolution: String)

data class SkipId(val id: String, val click: Click?)

data class SkipText(val text: String, val length: Int?)

data class SkipConfig(
    val skipTexts: MutableList<SkipText>?,
    val skipIds: MutableList<SkipId>?,
    val skipBounds: MutableList<Any>?
)

@Singleton
class SkipConfigRepository @Inject constructor() {
    suspend fun getTargetRect(rootNode: AccessibilityNodeInfo, activityName: String) =
        coroutineScope {
            val config = getConfig(activityName) ?: return@coroutineScope null

            select<Rect?> {
                config.skipIds?.forEach { skipId ->
                    async {
                        rootNode.findAccessibilityNodeInfosByViewId(skipId.id).firstOrNull()
                    }.onAwait {
                        LogUtils.d("findAccessibilityNodeInfosByViewId: ${it.toString()}")
                        if (it != null) {
                            if (skipId.click != null) {
                                val (xP, yP) = skipId.click.position.split(",")
                                val x = xP.toInt()
                                val y = yP.toInt()
                                val rect = Rect(
                                    x,
                                    y,
                                    x + 1,
                                    y + 1
                                )
                                LogUtils.d("rect: $rect")
                                rect
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

                config.skipTexts?.forEach { skipText ->
                    async {
                        val node =
                            rootNode.findAccessibilityNodeInfosByText(skipText.text).firstOrNull()
                        if (node != null && skipText.length is Int) {
                            if (node.text.length <= skipText.length) {
                                node
                            } else {
                                null
                            }
                        } else {
                            node
                        }
                    }.onAwait {
                        if (it != null) {
                            val rect = Rect()
                            it.getBoundsInScreen(rect)
                            rect
                        } else {
                            null
                        }
                    }
                }
            }
        }

    private fun getConfig(activityName: String): SkipConfig? {
        when (activityName) {
            "com.douban.frodo.activity.SplashActivity" -> {
                val skipIds: MutableList<SkipId> = mutableListOf()
                val click = Click(position = "1270,180", resolution = "")
                skipIds.add(SkipId(id = "com.douban.frodo:id/ad_mark", click = click))
                val config = SkipConfig(skipTexts = null, skipIds = skipIds, skipBounds = null)
                return config
            }
        }
        return null
    }
}