package com.android.skip.ui.inspect.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.skip.service.InspectService
import com.android.skip.util.AccessibilityState
import com.android.skip.util.AccessibilityStateUtils
import com.android.skip.util.MyToast
import com.blankj.utilcode.util.ServiceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartInspectViewModel @Inject constructor(): ViewModel() {
    private val _inspectState = MutableLiveData(ServiceUtils.isServiceRunning(InspectService::class.java))
    val inspectState: LiveData<Boolean> = _inspectState

    fun changeInspectState(isEnable: Boolean) {
        if (isEnable && AccessibilityStateUtils.getAccessibilityState() != AccessibilityState.STARTED) {
            MyToast.show("请先开启无障碍服务")
            return
        }
        _inspectState.value = isEnable
    }
}