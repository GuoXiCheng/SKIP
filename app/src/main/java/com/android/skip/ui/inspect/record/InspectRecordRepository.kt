package com.android.skip.ui.inspect.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.MyApp
import com.blankj.utilcode.util.FileUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectRecordRepository @Inject constructor() {
    private val _zipFileCount = MutableLiveData(0)
    var zipFileCount: LiveData<Int> = _zipFileCount

    fun changeZipFileCount() {
        val fileList = FileUtils.listFilesInDirWithFilter("${MyApp.context.filesDir}/capture") {
            it.name.endsWith("zip")
        }
        _zipFileCount.postValue(fileList.size)
    }
}