package com.android.skip.ui.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.skip.R
import com.android.skip.dataclass.SwitchThemeCarrier
import com.android.skip.dataclass.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SwitchThemeViewModel @Inject constructor() : ViewModel() {
    private val _currentTheme = MutableLiveData(
        SwitchThemeCarrier(
            themeMode = ThemeMode.UI_AUTO,
            themeName = R.string.settings_theme_auto,
            themeIcon = R.drawable.theme_auto
        )
    )
    val currentTheme: LiveData<SwitchThemeCarrier> = _currentTheme

    fun changeCurrentTheme(currentTheme: SwitchThemeCarrier) {
        _currentTheme.postValue(currentTheme)
    }
}