package com.android.skip.ui.main.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.service.InspectService
import com.android.skip.util.AccessibilityState
import com.android.skip.util.AccessibilityStateUtils
import com.blankj.utilcode.util.ServiceUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartAccessibilityRepository @Inject constructor() {
    private val _accessibilityState =
        MutableLiveData(AccessibilityStateUtils.getAccessibilityState())
    val accessibilityState: LiveData<AccessibilityState> = _accessibilityState

    fun changeAccessibilityState(state: AccessibilityState) {
        _accessibilityState.postValue(state)
        if (state != AccessibilityState.STARTED) {
            ServiceUtils.stopService(InspectService::class.java)
        }
    }
}