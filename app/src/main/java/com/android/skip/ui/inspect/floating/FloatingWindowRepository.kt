package com.android.skip.ui.inspect.floating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.service.InspectService
import com.blankj.utilcode.util.ServiceUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloatingWindowRepository @Inject constructor() {
    private val _floatingWindowState = MutableLiveData(ServiceUtils.isServiceRunning(InspectService::class.java))
    val floatingWindowState: LiveData<Boolean> = _floatingWindowState

    fun changeFloatingWindowState(state: Boolean) {
        _floatingWindowState.postValue(state)
    }
}