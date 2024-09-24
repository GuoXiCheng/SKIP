package com.android.skip.ui.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skip.R
import com.android.skip.dataclass.SwitchThemeCarrier
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SwitchThemeViewModel @Inject constructor(
    private val switchThemeRepository: SwitchThemeRepository
) : ViewModel() {
    val currentTheme: LiveData<SwitchThemeCarrier> = switchThemeRepository.currentTheme

    fun changeCurrentTheme(currentTheme: String) {
        switchThemeRepository.changeCurrentTheme(currentTheme)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataStoreUtils.putData(getString(R.string.store_current_theme), currentTheme)
            }
        }
    }
}