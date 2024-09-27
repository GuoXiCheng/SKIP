package com.android.skip.ui.main.disclaimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.skip.R
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DisclaimerViewModel @Inject constructor() : ViewModel() {
    private val _isShowDialog =
        MutableLiveData(DataStoreUtils.getSyncData(getString(R.string.store_show_disclaimer), true))
    val isShowDialog: LiveData<Boolean> = _isShowDialog

    fun changeDialogState(showDialog: Boolean) {
        _isShowDialog.postValue(showDialog)
    }
}