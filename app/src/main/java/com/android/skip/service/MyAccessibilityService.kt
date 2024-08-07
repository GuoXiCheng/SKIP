package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.graphics.Rect
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.SKIP_LAYOUT_INSPECT
import com.android.skip.SKIP_PERMIT_NOTICE
import com.android.skip.handler.BoundsHandler
import com.android.skip.handler.IdNodeHandler
import com.android.skip.handler.TextNodeHandler
import com.android.skip.manager.AnalyticsManager
import com.android.skip.manager.ToastManager
import com.android.skip.manager.WhitelistManager
import com.android.skip.utils.DataStoreUtils
import com.blankj.utilcode.util.LogUtils


class MyAccessibilityService : AccessibilityService() {
    private val textNodeHandler = TextNodeHandler()
    private val idNodeHandler = IdNodeHandler()
    private val boundsHandler = BoundsHandler()
    private var isLayoutInspect = false

    init {
        textNodeHandler.setNextHandler(idNodeHandler).setNextHandler(boundsHandler)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            event ?: return

            val rootNode = getCurrentRootNode()

            if (isLayoutInspect) {
                isLayoutInspect = false
                bfsTraverse(rootNode)
            }

            if (!AnalyticsManager.isPerformScan(rootNode.packageName.toString())) return

            if (WhitelistManager.isInWhitelist(rootNode.packageName.toString())) return

            val listOfRect = textNodeHandler.handle(rootNode)
            for (rect in listOfRect) {
                click(this, rect)
            }
        } catch (e: Exception) {
            // Log the exception or handle it in some other way
        } finally {
            AnalyticsManager.increaseScanCount()
        }
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        return rootInActiveWindow ?: throw IllegalStateException("No valid root node available")
    }

    override fun onInterrupt() {}

    private fun click(accessibilityService: AccessibilityService, rect: Rect) {
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        path.lineTo(rect.centerX().toFloat(), rect.centerY().toFloat())

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            .build()

        accessibilityService.dispatchGesture(
            gesture,
            object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)

                    if (AnalyticsManager.isShowToast()) {
                        if (DataStoreUtils.getSyncData(SKIP_PERMIT_NOTICE, false)) {
                            ToastManager.showToast(accessibilityService, "已为您跳过广告")
                        }
                        AnalyticsManager.setShowToastCount()
                    }

                }
            },
            null
        )
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event != null
            && event.action == KeyEvent.ACTION_DOWN
            && event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
            && DataStoreUtils.getSyncData(SKIP_LAYOUT_INSPECT, false)) {
            val intent = Intent(this, LayoutInspectService::class.java)
            intent.putExtra("keyCode", event.keyCode)
            startService(intent)

            isLayoutInspect = true
            return true
        }
        return super.onKeyEvent(event)
    }

    private fun bfsTraverse(root: AccessibilityNodeInfo) {
        val queue: MutableList<AccessibilityNodeInfo> = mutableListOf(root)
        val temp: MutableList<String> = mutableListOf()
        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            processNode(node, temp)

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.add(it) }
            }
        }
        LogUtils.d(temp.toString())
    }

    private fun processNode(node: AccessibilityNodeInfo, temp: MutableList<String>) {
        // 处理节点信息，可以进行读取文本属性、点击、长按等操作
        node.text?.let {
            temp.add(it.toString())
        }
        // 根据需要处理其它节点属性
    }
}