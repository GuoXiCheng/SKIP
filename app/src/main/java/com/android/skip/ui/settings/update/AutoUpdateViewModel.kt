package com.android.skip.ui.settings.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class AutoUpdateViewModel @Inject constructor() : ViewModel() {
    private val _autoUpdate =
        MutableLiveData(DataStoreUtils.getSyncData(getString(R.string.store_auto_update), false))
    val autoUpdate: LiveData<Boolean> = _autoUpdate

    fun changeAutoUpdate(autoUpdate: Boolean) {
        _autoUpdate.postValue(autoUpdate)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataStoreUtils.putData(getString(R.string.store_auto_update), autoUpdate)
            }
        }
    }
}