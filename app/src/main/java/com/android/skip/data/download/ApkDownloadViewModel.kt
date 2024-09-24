package com.android.skip.data.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ApkDownloadViewModel @Inject constructor(
    private val apkDownloadRepository: ApkDownloadRepository
) : ViewModel() {
    private val _isShowDialog = MutableLiveData(false)
    val isShowDialog: LiveData<Boolean> = _isShowDialog

    private val _apkDownloadProcess = MutableLiveData(0)
    val apkDownloadProcess: LiveData<Int> = _apkDownloadProcess

    fun changeDialogState(showDialog: Boolean) {
        _isShowDialog.postValue(showDialog)
    }

    private fun changeApkDownloadProcess(process: Int) {
        _apkDownloadProcess.postValue(process)
    }

    fun downloadAPK(latestVersion: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                apkDownloadRepository.downloadAPK(latestVersion) {
                    changeApkDownloadProcess(it)
                }
            }
        }
    }
}