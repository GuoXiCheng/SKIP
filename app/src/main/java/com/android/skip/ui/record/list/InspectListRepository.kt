package com.android.skip.ui.record.list

import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.dataclass.InspectRecordItem
import com.android.skip.dataclass.NodeRootSchema
import com.android.skip.ui.inspect.record.InspectRecordRepository
import com.android.skip.util.MyToast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectListRepository @Inject constructor() {

    @Inject
    lateinit var inspectRecordRepository: InspectRecordRepository

    fun getData(currentPage: Int, pageSize: Int): List<InspectRecordItem> {
        val fileList = FileUtils.listFilesInDirWithFilter("${MyApp.context.filesDir}/capture") {
            it.name.endsWith("zip")
        }

        val inspectRecordItemList = mutableListOf<InspectRecordItem>()
        fileList.forEach {
            val filename = it.name.substringBefore(".zip")
            val br =
                BufferedReader(FileReader("${MyApp.context.filesDir}/capture/${filename}.json"))
            val gson = Gson()
            val node = gson.fromJson(br, NodeRootSchema::class.java)

            val item = InspectRecordItem(
                node.fileId,
                AppUtils.getAppIcon(node.packageName)!!,
                node.appName,
                node.packageName,
                node.activityName.split(".").last(),
                node.createTime
            )

            inspectRecordItemList.add(item)
        }

        inspectRecordItemList.sortByDescending { it.createTime }

        val fromIndex = currentPage * pageSize
        val toIndex = minOf(fromIndex + pageSize, inspectRecordItemList.size)

        val pageData = inspectRecordItemList.subList(fromIndex, toIndex)

        return pageData
    }

    fun deleteByFileId(fileId: String) {
        val filePath = "${MyApp.context.filesDir}/capture/${fileId}"
        val zipFile = File("$filePath.zip")
        val jpegFile = File("$filePath.jpeg")
        val jsonFile = File("$filePath.json")
        val res1 = FileUtils.delete(zipFile)
        val res2 = FileUtils.delete(jsonFile)
        val res3 = FileUtils.delete(jpegFile)
        if (res1 && res2 && res3) {
            MyToast.show(R.string.toast_del_success)
            inspectRecordRepository.changeZipFileCount()
        } else {
            MyToast.show(R.string.toast_del_fail)
        }
    }

    fun deleteAllFile() {
        FileUtils.deleteFilesInDir("${MyApp.context.filesDir}/capture")
    }
}