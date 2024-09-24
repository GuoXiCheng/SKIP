package com.android.skip.ui.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.dataclass.SwitchThemeCarrier
import com.android.skip.dataclass.ThemeMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwitchThemeRepository @Inject constructor() {
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