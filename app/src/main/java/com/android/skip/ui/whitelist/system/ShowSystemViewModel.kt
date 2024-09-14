package com.android.skip.ui.whitelist.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowSystemViewModel @Inject constructor():ViewModel() {
    private val _isShowSystem = MutableLiveData(false)
    val isShowSystem: LiveData<Boolean> = _isShowSystem

    fun changeIsShowSystem(isShow: Boolean) {
        _isShowSystem.postValue(isShow)
    }
}