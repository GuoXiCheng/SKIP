package com.android.skip.ui.inspect.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InspectRecordViewModel @Inject constructor(private val repository: InspectRecordRepository): ViewModel() {
    val zipFileCount: LiveData<Int> = repository.zipFileCount

    init {
        changeZipFileCount()
    }

    fun changeZipFileCount() = repository.changeZipFileCount()
}