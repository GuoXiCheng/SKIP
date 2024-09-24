package com.android.skip.ui.settings.theme

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.dataclass.SwitchThemeCarrier
import com.android.skip.dataclass.ThemeMode
import com.android.skip.ui.components.FlatButtonMenu
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun SwitchThemeButton(switchThemeViewModel: SwitchThemeViewModel) {
    val currentTheme = switchThemeViewModel.currentTheme.observeAsState()
    val expandedState = remember { mutableStateOf(false) }

    FlatButtonMenu(content = {
        RowContent(
            title = R.string.settings_switch_theme,
            subTitle = currentTheme.value?.themeName,
            icon = { currentTheme.value?.themeIcon?.let { ResourceIcon(iconResource = it) } }
        )
    }, menuItems = {
        ThemeDropdownMenuItem(
            iconRes = R.drawable.theme_light,
            textRes = R.string.settings_theme_light,
            currentTheme = currentTheme,
            themeMode = ThemeMode.UI_LIGHT
        ) {
            expandedState.value = false
            switchThemeViewModel.changeCurrentTheme(ThemeMode.UI_LIGHT.name)
        }

        ThemeDropdownMenuItem(
            iconRes = R.drawable.theme_dark,
            textRes = R.string.settings_theme_dark,
            currentTheme = currentTheme,
            themeMode = ThemeMode.UI_DARK
        ) {
            expandedState.value = false
            switchThemeViewModel.changeCurrentTheme(ThemeMode.UI_DARK.name)
        }

        ThemeDropdownMenuItem(
            iconRes = R.drawable.theme_auto,
            textRes = R.string.settings_theme_auto,
            currentTheme = currentTheme,
            themeMode = ThemeMode.UI_AUTO
        ) {
            expandedState.value = false
            switchThemeViewModel.changeCurrentTheme(ThemeMode.UI_AUTO.name)
        }
    }, expanded = expandedState)
}

@Composable
private fun ThemeDropdownMenuItem(
    iconRes: Int,
    textRes: Int,
    currentTheme: State<SwitchThemeCarrier?>,
    themeMode: ThemeMode,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        leadingIcon = { ResourceIcon(iconResource = iconRes) },
        text = {
            Text(
                text = stringResource(id = textRes),
                color = if (currentTheme.value?.themeMode == themeMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        },
        onClick = onClick
    )
}
