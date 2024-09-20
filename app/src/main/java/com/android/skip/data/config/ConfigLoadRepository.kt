package com.android.skip.data.config

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.R
import com.android.skip.dataclass.ConfigLoadSchema
import com.android.skip.dataclass.LoadSkipBound
import com.android.skip.dataclass.LoadSkipId
import com.android.skip.dataclass.LoadSkipText
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class ConfigLoadRepository @Inject constructor() {
    private var configLoadSchemaMap: Map<String, ConfigLoadSchema> = mutableMapOf()

    fun loadConfig(config: Map<String, ConfigLoadSchema>) {
        configLoadSchemaMap = config
    }

    suspend fun getTargetRect(
        rootNode: AccessibilityNodeInfo, activityName: String?, isStrict: Boolean?
    ): Rect? {
        val targetAppPackage = rootNode.packageName.toString()
        var targetConfig = configLoadSchemaMap[targetAppPackage]

        if (isStrict != true) {
            val defaultSkipTexts = listOf(
                LoadSkipText(text = getString(R.string.settings_strict_skip))
            )
            if (targetConfig == null) {
                targetConfig = ConfigLoadSchema(targetAppPackage, defaultSkipTexts)
            } else if (targetConfig.skipTexts == null) {
                targetConfig.skipTexts = defaultSkipTexts
            }
        }

        return withContext(Dispatchers.Main) {
            try {
                val skipByTextTasks =
                    createSkipByTextTasks(this, rootNode, targetConfig?.skipTexts, activityName)
                val skipByIdTasks =
                    createSkipByIdTasks(this, rootNode, targetConfig?.skipIds, activityName)
                val skipByBoundTasks =
                    createSkipByBoundTasks(this, rootNode, targetConfig?.skipBounds, activityName)
                val tasks = skipByTextTasks + skipByIdTasks + skipByBoundTasks
                awaitFirstNonNullOrComplete(this, tasks)
            } catch (e: Exception) {
                LogUtils.e(e)
                null
            }
        }
    }

    private suspend fun <T> awaitFirstNonNullOrComplete(
        scope: CoroutineScope, tasks: List<Deferred<T?>>
    ): T? {
        val deferredResults = mutableListOf<Deferred<T?>>()

        tasks.forEach { deferred ->
            deferredResults.add(scope.async {
                val result = deferred.await()
                if (result != null) {
                    return@async result
                }
                null
            })
        }

        deferredResults.forEach { result ->
            val nonNullResult = result.await()
            if (nonNullResult != null) {
                return nonNullResult
            }
        }

        return null
    }

    private fun createSkipByTextTasks(
        scope: CoroutineScope,
        rootNode: AccessibilityNodeInfo,
        skipTexts: List<LoadSkipText>?,
        activityName: String?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipTexts.isNullOrEmpty()) return deferredResults

        for (skipText in skipTexts) {
            if (skipText.activityName != null && skipText.activityName != activityName) continue
            deferredResults.add(scope.async {
                val foundNode =
                    rootNode.findAccessibilityNodeInfosByText(skipText.text).firstOrNull()

                val targetNode = if (foundNode != null) {
                    if (skipText.length != null) {
                        if (foundNode.text.length <= skipText.length) {
                            foundNode
                        } else {
                            null
                        }
                    } else {
                        foundNode
                    }
                } else {
                    null
                }

                if (targetNode != null) {
                    if (skipText.click != null) {
                        skipText.click
                    } else {
                        val rect = Rect()
                        targetNode.getBoundsInScreen(rect)
                        rect
                    }
                } else {
                    null
                }
            })
        }
        return deferredResults
    }

    private fun createSkipByIdTasks(
        scope: CoroutineScope,
        rootNode: AccessibilityNodeInfo,
        skipIds: List<LoadSkipId>?,
        activityName: String?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipIds.isNullOrEmpty()) return deferredResults

        for (skipId in skipIds) {
            if (skipId.activityName != null && skipId.activityName != activityName) continue
            deferredResults.add(scope.async {
                val foundNode = rootNode.findAccessibilityNodeInfosByViewId(skipId.id).firstOrNull()

                if (foundNode != null) {
                    if (skipId.click != null) {
                        skipId.click
                    } else {
                        val rect = Rect()
                        foundNode.getBoundsInScreen(rect)
                        rect
                    }
                } else {
                    null
                }
            })
        }
        return deferredResults
    }

    private fun createSkipByBoundTasks(
        scope: CoroutineScope,
        rootNode: AccessibilityNodeInfo,
        skipBounds: List<LoadSkipBound>?,
        activityName: String?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipBounds.isNullOrEmpty()) return deferredResults

        for (skipBound in skipBounds) {
            if (skipBound.activityName != null && skipBound.activityName != activityName) continue

            deferredResults.add(scope.async {
                val foundRect = traverseNode(rootNode, skipBound.bound)

                skipBound.click ?: foundRect
            })
        }
        return deferredResults
    }

    private fun traverseNode(
        rootNode: AccessibilityNodeInfo, targetRect: Rect
    ): Rect? {
        val queue: MutableList<AccessibilityNodeInfo> = mutableListOf(rootNode)

        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            val nodeRect = Rect()
            node.getBoundsInScreen(nodeRect)
            if (abs(nodeRect.left - targetRect.left) <= 1 && abs(nodeRect.top - targetRect.top) <= 1 && abs(
                    nodeRect.right - targetRect.right
                ) <= 1 && abs(nodeRect.bottom - targetRect.bottom) <= 1
            ) {
                return nodeRect
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let {
                    queue.add(it)
                }
            }
        }
        return null
    }
}