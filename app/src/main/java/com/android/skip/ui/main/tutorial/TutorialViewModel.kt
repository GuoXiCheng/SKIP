package com.android.skip.ui.main.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor() : ViewModel() {
    private val _isShowDialog = MutableLiveData<Boolean>()
    val isShowDialog: LiveData<Boolean> = _isShowDialog

    fun changeDialogState(showDialog: Boolean) {
        _isShowDialog.postValue(showDialog)
    }
}