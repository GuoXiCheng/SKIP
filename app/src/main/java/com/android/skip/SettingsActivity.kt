package com.android.skip

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.android.skip.compose.FlatButton
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.ui.theme.themeTypeState
import com.android.skip.utils.DataStoreUtils

class SettingsActivity : BaseActivity() {
    @Composable
    override fun ProvideContent() {
        SettingsActivityInterface(onBackClick = { finish() })
    }
}

@Composable
fun SettingsActivityInterface(onBackClick: () -> Unit) {
    val expandedState = remember { mutableStateOf(false) }
    val options = listOf("浅色模式", "深色模式", "跟随系统")
    val selectedState = remember { mutableStateOf(0) }
    val checkUpdateVersion = remember { mutableStateOf(true) }
    val checkUpdateConfig = remember { mutableStateOf(true) }

    ScaffoldPage(
        barTitle = stringResource(id = R.string.settings),
        onBackClick = onBackClick, content = {
            FlatButton(
                content = {
                    RowContent(
                        "自动检查更新",
                        "打开应用时自动检查新版本",
                        R.drawable.cloud_download,
                        checkUpdateVersion
                    )
                })

            FlatButton(
                content = {
                    RowContent(
                        "自动同步配置",
                        "打开应用时自动获取并更新配置文件",
                        R.drawable.sync,
                        checkUpdateConfig
                    )
                })

            Menu(expandedState, options, selectedState)
            FlatButton(
                content = {
                    when (themeTypeState.value) {
                        Configuration.UI_MODE_NIGHT_NO -> RowContent(
                            options[0], "保持明亮模式", R.drawable.brightness_5
                        )

                        Configuration.UI_MODE_NIGHT_YES -> RowContent(
                            options[1], "保持暗黑模式", R.drawable.brightness_4
                        )

                        else -> RowContent(
                            options[2], "跟随系统设置", R.drawable.brightness_6
                        )
                    }
                }) {
                expandedState.value = !expandedState.value
            }


        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    expandedState: MutableState<Boolean>,
    options: List<String>,
    selectedState: MutableState<Int>
) {
    val themeMapping = mapOf(
        0 to Configuration.UI_MODE_NIGHT_NO,
        1 to Configuration.UI_MODE_NIGHT_YES,
        2 to Configuration.UI_MODE_NIGHT_UNDEFINED
    )

    ExposedDropdownMenuBox(
        expanded = expandedState.value,
        onExpandedChange = { expandedState.value = !expandedState.value }
    ) {
        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false },
            offset = DpOffset(x = 100.dp, y = 0.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = themeMapping[index] == themeTypeState.value
                DropdownMenuItem(
                    modifier = Modifier.background(if (isSelected) MaterialTheme.colorScheme.background else Color.Transparent),
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        selectedState.value = index
                        expandedState.value = false
                        updateTheme(themeMapping[index])
                    }
                )
            }
        }
    }
}

private fun updateTheme(themeMode: Int?) {
    themeMode?.let {
        themeTypeState.value = it
        DataStoreUtils.putSyncData(SKIP_APP_THEME, it)
    }
}

