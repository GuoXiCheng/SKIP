package com.android.skip.ui.settings.tip

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
class TipViewModel @Inject constructor(
    private val tipRepository: TipRepository
) : ViewModel() {
    val enable = tipRepository.enable
    fun changeEnable(enable: Boolean) {
        tipRepository.changeEnable(enable)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataStoreUtils.putData(getString(R.string.store_skip_tip), enable)
            }
        }
    }
}