package com.android.skip.data.config

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.dataclass.ConfigLoadSchema
import com.android.skip.dataclass.LoadSkipBound
import com.android.skip.dataclass.LoadSkipId
import com.android.skip.dataclass.LoadSkipText
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class ConfigLoadRepository2 @Inject constructor() {
    private lateinit var configLoadSchemaMap: Map<String, ConfigLoadSchema>

    fun loadConfig(config: Map<String, ConfigLoadSchema>) {
        configLoadSchemaMap = config
    }

    suspend fun getTargetRect(rootNode: AccessibilityNodeInfo): Rect? {
        val targetConfig = configLoadSchemaMap[rootNode.packageName]

        return withContext(Dispatchers.Default) {
            try {
                val skipByTextTasks = createSkipByTextTasks(this, rootNode, targetConfig?.skipTexts)
                val skipByIdTasks = createSkipByIdTasks(this, rootNode, targetConfig?.skipIds)
                val skipByBoundTasks =
                    createSkipByBoundTasks(this, rootNode, targetConfig?.skipBounds)
                val tasks = skipByTextTasks + skipByIdTasks + skipByBoundTasks
                awaitFirstNonNullOrComplete(this, tasks)
            } catch (e: Exception) {
                LogUtils.e(e)
                null
            }
        }
    }

    private suspend fun <T> awaitFirstNonNullOrComplete(
        scope: CoroutineScope,
        tasks: List<Deferred<T?>>
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
        skipTexts: List<LoadSkipText>?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipTexts.isNullOrEmpty()) return deferredResults

        skipTexts.forEach { skipText ->
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
                    val rect = Rect()
                    targetNode.getBoundsInScreen(rect)
                    rect
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
        skipIds: List<LoadSkipId>?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipIds.isNullOrEmpty()) return deferredResults

        skipIds.forEach { skipId ->
            deferredResults.add(scope.async {
                val foundNode = rootNode.findAccessibilityNodeInfosByViewId(skipId.id).firstOrNull()

                if (foundNode != null) {
                    val rect = Rect()
                    foundNode.getBoundsInScreen(rect)
                    rect
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
        skipBounds: List<LoadSkipBound>?
    ): List<Deferred<Rect?>> {
        val deferredResults = mutableListOf<Deferred<Rect?>>()
        if (skipBounds.isNullOrEmpty()) return deferredResults

        skipBounds.forEach { skipBound ->
            deferredResults.add(scope.async {
                val foundNode = traverseNode(rootNode, skipBound.bound)

                if (foundNode != null) {
                    val rect = Rect()
                    foundNode.getBoundsInScreen(rect)
                    rect
                } else {
                    null
                }
            })
        }

        return deferredResults
    }

    private fun traverseNode(
        rootNode: AccessibilityNodeInfo,
        targetRect: Rect
    ): AccessibilityNodeInfo? {
        val queue: MutableList<AccessibilityNodeInfo> = mutableListOf(rootNode)

        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            val nodeRect = Rect()
            node.getBoundsInScreen(nodeRect)
            if (abs(nodeRect.left - targetRect.left) <= 1
                && abs(nodeRect.top - targetRect.top) <= 1
                && abs(nodeRect.right - targetRect.right) <= 1
                && abs(nodeRect.bottom - targetRect.bottom) <= 1
            ) {
                return node
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