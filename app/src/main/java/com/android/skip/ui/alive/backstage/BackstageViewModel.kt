package com.android.skip.ui.alive.backstage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BackstageViewModel @Inject constructor() : ViewModel() {
    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> = _showDialog

    fun changeDialogState(isShow: Boolean) {
        _showDialog.postValue(isShow)
    }
}