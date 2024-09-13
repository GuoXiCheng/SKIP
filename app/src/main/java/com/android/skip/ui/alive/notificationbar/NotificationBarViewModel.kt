package com.android.skip.ui.alive.notificationbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationBarViewModel @Inject constructor(
    private val notificationBarRepository:NotificationBarRepository
) : ViewModel() {
    val enable = notificationBarRepository.enable

    fun changeEnable(enable: Boolean) = notificationBarRepository.changeEnable(enable)
}