package com.android.skip.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityNodeInfo
import com.android.skip.SKIPApp
import com.android.skip.dataclass.AccessibilityNodeInfoCarrier
import com.android.skip.dataclass.NodeChildSchema
import com.android.skip.dataclass.NodeRootSchema
import com.android.skip.manager.ToastManager
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ZipUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import kotlin.properties.Delegates

object LayoutInspectUtils {
    private var fileId: Long? = null
    private var isStartInspectNode: Boolean = false
    private var isStartScreenCapture: Boolean = false
    private var activityName: String? = null

    fun startLayoutInspect() {
        fileId = System.currentTimeMillis()
        isStartInspectNode = true
        isStartScreenCapture = true

        CoroutineScope(Dispatchers.IO).launch {
            repeat(3) { index ->
                delay(3000)
                val zipFile = File(SKIPApp.context.filesDir, "$fileId.zip")
                if (FileUtils.isFileExists(zipFile)) return@repeat

                val pngFile = File(SKIPApp.context.filesDir, "$fileId.png")
                val jsonFile = File(SKIPApp.context.filesDir, "$fileId.json")
                if (FileUtils.isFileExists(pngFile) && FileUtils.isFileExists(jsonFile)) {
                    ZipUtils.zipFiles(
                        listOf(
                            pngFile, jsonFile
                        ), zipFile
                    )
                    ToastManager.showToast(SKIPApp.context, "保存成功")
                } else {
                    if (index == 2) {
                        ToastManager.showToast(SKIPApp.context, "保存失败")
                    }
                }
            }
        }
    }

    fun startRecordNodeInfo(rootNode: AccessibilityNodeInfo, className: CharSequence?) {
        className?.let {
            val classNameStr = it.toString()
            if (!AccessibilityUtils.isSystemClass(classNameStr)) activityName = classNameStr
        }

        if (isStartInspectNode) {
            isStartInspectNode = false
            bfsTraverse(rootNode)
        }
    }

    @SuppressLint("WrongConstant")
    fun startScreenCapture(imageReader: ImageReader, displayWidth: Int, displayHeight: Int) {
        imageReader.setOnImageAvailableListener({ reader ->
            if (!isStartScreenCapture) {
                // 清理缓冲区
                reader.acquireLatestImage()?.close()
                return@setOnImageAvailableListener
            }

            val image = reader.acquireLatestImage()
            if (image != null) {
                val planes = image.planes
                val buffer = planes[0].buffer
                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride

                // Create Bitmap
                val bitmapWithStride = Bitmap.createBitmap(
                    rowStride / pixelStride, displayHeight, Bitmap.Config.ARGB_8888
                )
                bitmapWithStride.copyPixelsFromBuffer(buffer)
                val bitmap =
                    Bitmap.createBitmap(bitmapWithStride, 0, 0, displayWidth, displayHeight)
                image.close()

                // 保存或处理bitmap
                val file = File(SKIPApp.context.filesDir, "$fileId.png")
                saveBitmapToFile(bitmap, file)
                isStartScreenCapture = false
            }
        }, Handler(Looper.getMainLooper()))
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun bfsTraverse(root: AccessibilityNodeInfo) {
        var uniqueNodeId = 0
        val queue: MutableList<AccessibilityNodeInfoCarrier> = mutableListOf(
            AccessibilityNodeInfoCarrier(root, 0, -1, uniqueNodeId)
        )
        val nodeChildSchemaList: MutableList<NodeChildSchema> = mutableListOf()

        while (queue.isNotEmpty()) {
            val (node, depth, parentId, nodeId) = queue.removeAt(0)
            processNode(node, nodeChildSchemaList, depth, parentId, nodeId)

            for (i in 0 until node.childCount) {
                uniqueNodeId += 1
                node.getChild(i)?.let {
                    queue.add(
                        AccessibilityNodeInfoCarrier(
                            it, depth + 1, nodeId, uniqueNodeId
                        )
                    )
                }
            }
        }

        val nodeRootSchema = NodeRootSchema(
            fileId!!,
            InstalledAppUtils.getAppInfoByPackageName(root.packageName.toString()).name,
            root.packageName.toString(),
            activityName.toString(),
            ScreenUtils.getScreenHeight(),
            ScreenUtils.getScreenWidth(),
            nodeChildSchemaList
        )

        val gson = Gson()
        val jsonStr = gson.toJson(nodeRootSchema)
        val file = File(SKIPApp.context.filesDir, "$fileId.json")
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
}