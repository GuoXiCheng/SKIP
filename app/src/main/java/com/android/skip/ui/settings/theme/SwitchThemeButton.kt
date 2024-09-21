package com.android.skip.ui.settings.theme

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        DropdownMenuItem(
            leadingIcon = { ResourceIcon(iconResource = R.drawable.theme_light) },
            text = {
                Text(
                    text = stringResource(id = R.string.settings_theme_light),
                    color = if (currentTheme.value?.themeMode == ThemeMode.UI_LIGHT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            },
            onClick = {
                expandedState.value = false
                switchThemeViewModel.changeCurrentTheme(
                    SwitchThemeCarrier(
                        themeMode = ThemeMode.UI_LIGHT,
                        themeName = R.string.settings_theme_light,
                        themeIcon = R.drawable.theme_light
                    )
                )
            })
        DropdownMenuItem(
            leadingIcon = { ResourceIcon(iconResource = R.drawable.theme_dark) },
            text = {
                Text(
                    text = stringResource(id = R.string.settings_theme_dark),
                    color = if (currentTheme.value?.themeMode == ThemeMode.UI_DARK) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            },
            onClick = {
                expandedState.value = false
                switchThemeViewModel.changeCurrentTheme(
                    SwitchThemeCarrier(
                        themeMode = ThemeMode.UI_DARK,
                        themeName = R.string.settings_theme_dark,
                        themeIcon = R.drawable.theme_dark
                    )
                )
            })
        DropdownMenuItem(
            leadingIcon = { ResourceIcon(iconResource = R.drawable.theme_auto) },
            text = {
                Text(
                    text = stringResource(id = R.string.settings_theme_auto),
                    color = if (currentTheme.value?.themeMode == ThemeMode.UI_AUTO) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            },
            onClick = {
                expandedState.value = false

                switchThemeViewModel.changeCurrentTheme(
                    SwitchThemeCarrier(
                        themeMode = ThemeMode.UI_AUTO,
                        themeName = R.string.settings_theme_auto,
                        themeIcon = R.drawable.theme_auto
                    )
                )
            })
    }, expanded = expandedState)
}