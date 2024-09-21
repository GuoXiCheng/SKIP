package com.android.skip.dataclass

enum class ThemeMode {
    UI_LIGHT,UI_DARK, UI_AUTO
}

data class SwitchThemeCarrier(val themeMode: ThemeMode, val themeName: Int, val themeIcon: Int)
