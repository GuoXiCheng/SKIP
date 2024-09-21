package com.android.skip.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.android.skip.dataclass.ThemeMode
import com.android.skip.ui.settings.theme.SwitchThemeViewModel

private val darkScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Black,
    onBackground = White
)

private val lightScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = White,
    onBackground = Black
)

@Composable
fun AppTheme(
    switchThemeViewModel: SwitchThemeViewModel,
    content: @Composable ()->Unit
) {
    val currentTheme = switchThemeViewModel.currentTheme.observeAsState()
    val darkTheme = when(currentTheme.value?.themeMode) {
        ThemeMode.UI_LIGHT -> false
        ThemeMode.UI_DARK -> true
        else -> isSystemInDarkTheme()
    }
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme
//            !darkTheme || themeTypeState.value != Configuration.UI_MODE_NIGHT_YES
        }
    }

    MaterialTheme (
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}