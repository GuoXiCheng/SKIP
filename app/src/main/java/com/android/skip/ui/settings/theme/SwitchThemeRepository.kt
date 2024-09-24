package com.android.skip.ui.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.dataclass.SwitchThemeCarrier
import com.android.skip.dataclass.ThemeMode
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwitchThemeRepository @Inject constructor() {
    private val _currentTheme = MutableLiveData(
        getThemeCarrier(
            DataStoreUtils.getSyncData(
                getString(R.string.store_current_theme),
                ThemeMode.UI_AUTO.toString()
            )
        )
    )
    val currentTheme: LiveData<SwitchThemeCarrier> = _currentTheme

    fun changeCurrentTheme(currentTheme: String) {
        _currentTheme.postValue(getThemeCarrier(currentTheme))
    }

    private fun getThemeCarrier(theme: String): SwitchThemeCarrier {
        return when (theme) {
            ThemeMode.UI_LIGHT.toString() -> SwitchThemeCarrier(
                themeMode = ThemeMode.UI_LIGHT,
                themeName = R.string.settings_theme_light,
                themeIcon = R.drawable.theme_light
            )

            ThemeMode.UI_DARK.toString() -> SwitchThemeCarrier(
                themeMode = ThemeMode.UI_DARK,
                themeName = R.string.settings_theme_dark,
                themeIcon = R.drawable.theme_dark
            )

            else -> SwitchThemeCarrier(
                themeMode = ThemeMode.UI_AUTO,
                themeName = R.string.settings_theme_auto,
                themeIcon = R.drawable.theme_auto
            )
        }
    }
}