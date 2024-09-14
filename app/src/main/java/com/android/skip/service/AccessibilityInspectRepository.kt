package com.android.skip.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.ui.inspect.record.InspectRecordRepository
import com.android.skip.util.MyToast
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ZipUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID.randomUUID


@Singleton
class AccessibilityInspectRepository @Inject constructor() {
    @Inject
    lateinit var inspectRecordRepository: InspectRecordRepository

    private val _accessibilityInspectSuccess = MutableLiveData<Long>()
    val accessibilityInspectSuccess: LiveData<Long> = _accessibilityInspectSuccess

    private var _fileId: String = "none"
    val fileId: String
        get() = _fileId

    private var _isStartCaptureNode: Boolean = false
    val isStartCaptureNode: Boolean
        get() = _isStartCaptureNode

    private var _isStartCaptureScreen: Boolean = false
    val isStartCaptureScreen: Boolean
        get() = _isStartCaptureScreen

    val filepath = "${MyApp.context.filesDir}/capture"

    fun stopCaptureNode() {
        _isStartCaptureNode = false
    }

    fun stopCaptureScreen() {
        _isStartCaptureScreen = false
    }

    fun startAccessibilityInspect() {
        val fileDir = File(filepath)
        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        _isStartCaptureScreen = true
        _isStartCaptureNode = true
        _fileId = randomUUID().toString()

        CoroutineScope(Dispatchers.IO).launch {
            val repeatTimes = 5
            repeat(repeatTimes) { index ->
                delay(1000)
                val zipFile = File(filepath, "$fileId.zip")
                if (FileUtils.isFileExists(zipFile)) return@repeat

                val jpegFile = File(filepath, "$fileId.jpeg")
                val jsonFile = File(filepath, "$fileId.json")
                if (FileUtils.isFileExists(jpegFile) && FileUtils.isFileExists(jsonFile)) {
                    ZipUtils.zipFiles(
                        listOf(
                            jpegFile, jsonFile
                        ), zipFile
                    )
                    MyToast.show(R.string.toast_save_success)
                    _accessibilityInspectSuccess.postValue(System.currentTimeMillis())
                } else {
                    if (index == repeatTimes - 1) {
                        MyToast.show(R.string.toast_save_fail)
                    }
                }
            }
        }
    }
}