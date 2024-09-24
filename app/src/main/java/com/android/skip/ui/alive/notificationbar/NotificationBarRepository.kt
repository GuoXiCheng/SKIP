package com.android.skip.ui.alive.notificationbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationBarRepository @Inject constructor() {
    private val _enable = MutableLiveData(
        DataStoreUtils.getSyncData(getString(R.string.store_resident_notification_bar), false)
    )
    val enable: LiveData<Boolean> = _enable

    fun changeEnable(enable: Boolean) {
        _enable.postValue(enable)
    }
}