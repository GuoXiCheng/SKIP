package com.android.skip.ui.whitelist

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
class WhiteListViewModel @Inject constructor(
    private val whiteListRepository: WhiteListRepository
) : ViewModel() {
    fun updateWhiteList(packageName: String, checked: Boolean) {
        whiteListRepository.updateWhiteList(packageName, checked)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataStoreUtils.putData(getString(R.string.whitelist_dot) + packageName, checked)
            }
        }
    }
}