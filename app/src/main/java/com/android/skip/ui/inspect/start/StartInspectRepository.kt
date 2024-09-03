package com.android.skip.ui.inspect.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.util.AccessibilityState
import com.android.skip.util.AccessibilityStateUtils
import com.android.skip.util.MyToast
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartInspectRepository @Inject constructor() {
    private val _inspectState = MutableLiveData(false)
    val inspectState: LiveData<Boolean> = _inspectState

    fun changeInspectState(state: Boolean) {
        if (state && AccessibilityStateUtils.getAccessibilityState() != AccessibilityState.STARTED) {
            MyToast.show("请先开启无障碍服务")
            return
        }
        _inspectState.postValue(state)
    }
}