package com.android.skip.ui.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.skip.dataclass.SwitchThemeCarrier
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SwitchThemeViewModel @Inject constructor(
    private val switchThemeRepository: SwitchThemeRepository
) : ViewModel() {
    val currentTheme: LiveData<SwitchThemeCarrier> = switchThemeRepository.currentTheme

    fun changeCurrentTheme(currentTheme: SwitchThemeCarrier) =
        switchThemeRepository.changeCurrentTheme(currentTheme)
}