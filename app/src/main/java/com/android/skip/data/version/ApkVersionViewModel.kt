package com.android.skip.data.version

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApkVersionViewModel @Inject constructor(
    private val apkVersionRepository: ApkVersionRepository
) : ViewModel() {
    val versionPostState = apkVersionRepository.versionPostState

    fun checkVersion() {
        viewModelScope.launch {
            apkVersionRepository.checkVersion()
        }
    }
}