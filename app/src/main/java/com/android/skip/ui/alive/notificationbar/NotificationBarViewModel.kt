package com.android.skip.ui.alive.notificationbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skip.R
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationBarViewModel @Inject constructor(
    private val notificationBarRepository:NotificationBarRepository
) : ViewModel() {
    val enable = notificationBarRepository.enable

    fun changeEnable(enable: Boolean) {
        notificationBarRepository.changeEnable(enable)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataStoreUtils.putSyncData(getString(R.string.store_resident_notification_bar), enable)
            }
        }
    }
}