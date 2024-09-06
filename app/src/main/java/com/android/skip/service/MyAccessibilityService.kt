package com.android.skip.service

import android.accessibilityservice.AccessibilityService
import android.content.pm.PackageManager
import android.graphics.Rect
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.MyApp
import com.android.skip.dataclass.AccessibilityNodeInfoCarrier
import com.android.skip.dataclass.NodeChildSchema
import com.android.skip.dataclass.NodeRootSchema
import com.android.skip.ui.main.start.StartAccessibilityRepository
import com.android.skip.util.AccessibilityState
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ServiceUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MyAccessibilityService : AccessibilityService() {
    private var activityName: String? = null

    @Inject
    lateinit var repository: StartAccessibilityRepository

    @Inject
    lateinit var accessibilityInspectRepository: AccessibilityInspectRepository

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            val rootNode = getCurrentRootNode()

            getActivityName(event)?.let{
                activityName = it
            }

            activityName?.let {
                if (accessibilityInspectRepository.isStartCaptureNode) {
                    startCaptureNode(rootNode, it)
                    accessibilityInspectRepository.stopCaptureNode()
                }
            }

        } catch (e: Exception) {
//            LogUtils.e(e)
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        repository.changeAccessibilityState(AccessibilityState.STARTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.changeAccessibilityState(AccessibilityState.STOPPED)
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        val isServiceRunning = ServiceUtils.isServiceRunning(InspectService::class.java)
        if (event != null
            && event.action == KeyEvent.ACTION_DOWN
            && event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
            && isServiceRunning
        ) {
            LogUtils.d("onKeyEvent")
            accessibilityInspectRepository.startAccessibilityInspect()
            return true
        }
        return super.onKeyEvent(event)
    }

    private fun getCurrentRootNode(): AccessibilityNodeInfo {
        return rootInActiveWindow ?: throw IllegalStateException("No valid root node available")
    }

    private fun isSystemClass(className: String): Boolean {
        return try {
            val clazz = Class.forName(className)
            val packageName = clazz.getPackage()?.name
            packageName != null && (packageName.startsWith("android") || packageName.startsWith("androidx"))
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private fun getActivityName(event: AccessibilityEvent?): String? {
        event ?: return null
        val className = event.className

        className ?: return null
        val classNameStr = className.toString()

        val isSystem = isSystemClass(classNameStr)
        return if (!isSystem) classNameStr else null
    }

    private fun startCaptureNode(rootNode: AccessibilityNodeInfo, className: String) {
        var uniqueNodeId = 0
        val queue: MutableList<AccessibilityNodeInfoCarrier> = mutableListOf(
            AccessibilityNodeInfoCarrier(rootNode, 0, -1, uniqueNodeId)
        )
        val nodeChildSchemaList: MutableList<NodeChildSchema> = mutableListOf()

        while (queue.isNotEmpty()) {
            val (node, depth, parentId, nodeId) = queue.removeAt(0)
            processNode(node, nodeChildSchemaList, depth, parentId, nodeId)

            for (i in 0 until node.childCount) {
                uniqueNodeId += 1
                node.getChild(i)?.let {
                    queue.add(
                        AccessibilityNodeInfoCarrier(it, depth + 1, nodeId, uniqueNodeId)
                    )
                }
            }
        }

        val nodeRootSchema = NodeRootSchema(
            accessibilityInspectRepository.fileId,
            getAppName(rootNode.packageName.toString()),
            rootNode.packageName.toString(),
            className,
            ScreenUtils.getScreenHeight(),
            ScreenUtils.getScreenWidth(),
            System.currentTimeMillis(),
            nodeChildSchemaList
        )

        val gson = Gson()
        val jsonStr = gson.toJson(nodeRootSchema)
        val file = File(
            accessibilityInspectRepository.filepath,
            "${accessibilityInspectRepository.fileId}.json"
        )
        file.writeText(jsonStr)
    }

    private fun processNode(
        node: AccessibilityNodeInfo,
        nodeChildSchemaList: MutableList<NodeChildSchema>,
        depth: Int,
        parentId: Int,
        nodeId: Int
    ) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val myNodeChild = NodeChildSchema(
            depth,
            node.childCount,
            parentId,
            nodeId,
            rect.left,
            rect.top,
            rect.right,
            rect.bottom,
            node.isClickable
        )

        node.className?.let {
            myNodeChild.className = it.toString()
        }

        node.text?.let {
            myNodeChild.text = it.toString()
        }

        node.viewIdResourceName?.let {
            myNodeChild.viewIdResourceName = it
        }

        nodeChildSchemaList.add(myNodeChild)
    }

    private fun getAppName(packageName: String): String {
        return try {
            val context = MyApp.context
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(applicationInfo).toString()
            appName
        } catch (e: PackageManager.NameNotFoundException) {
            "com.unknown.app"
        }
    }

}