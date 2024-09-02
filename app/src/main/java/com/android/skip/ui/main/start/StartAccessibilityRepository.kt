package com.android.skip.ui.main.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.util.AccessibilityState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartAccessibilityRepository @Inject constructor() {
    private val _accessibilityState = MutableLiveData<AccessibilityState>()
    val accessibilityState: LiveData<AccessibilityState> = _accessibilityState

    fun changeAccessibilityState(state: AccessibilityState) {
        _accessibilityState.postValue(state)
    }
}