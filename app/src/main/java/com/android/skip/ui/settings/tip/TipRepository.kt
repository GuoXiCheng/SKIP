package com.android.skip.ui.settings.tip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipRepository @Inject constructor(){
    private val _enable = MutableLiveData(false)
    val enable: LiveData<Boolean> = _enable

    fun changeEnable(enable: Boolean) {
        _enable.postValue(enable)
    }
}