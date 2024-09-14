package com.android.skip.service

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccessibilityInspectViewModel @Inject constructor(
    accessibilityInspectRepository: AccessibilityInspectRepository,
) : ViewModel() {
    val accessibilityInspectSuccess = accessibilityInspectRepository.accessibilityInspectSuccess
}