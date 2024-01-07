package com.android.skip

import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.android.skip.compose.FlatButton
import com.android.skip.compose.ResourceIcon
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.utils.DataStoreUtils



class SettingsActivity : BaseActivity() {
    @Composable
    override fun ProvideContent() {
        SettingsActivityInterface(onBackClick = { finish() })
    }

    override fun onResume() {
        super.onResume()
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            permitNoticeState.value = false
            DataStoreUtils.putSyncData(SKIP_PERMIT_NOTICE, false)
        }
    }
}

@Composable
fun SettingsActivityInterface(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val expandedState = remember { mutableStateOf(false) }
    val options = listOf(
        stringResource(id = R.string.settings_pattern_light_title),
        stringResource(id = R.string.settings_pattern_dark_title),
        stringResource(id = R.string.settings_pattern_system_title)
    )
    val selectedState = remember { mutableStateOf(0) }
    val checkUpdateVersion = remember {
        mutableStateOf(
            DataStoreUtils.getSyncData(
                SKIP_AUTO_CHECK_UPDATE, false
            )
        )
    }
    val checkUpdateConfig = remember {
        mutableStateOf(
            DataStoreUtils.getSyncData(
                SKIP_AUTO_SYNC_CONFIG, false
            )
        )
    }

    ScaffoldPage(
        barTitle = stringResource(id = R.string.settings),
        onBackClick = onBackClick, content = {
            FlatButton(
                content = {
                    RowContent(
                        stringResource(id = R.string.settings_check_update_title),
                        stringResource(id = R.string.settings_check_update_subtitle),
                        { ResourceIcon(iconResource = R.drawable.cloud_download) },
                        checkUpdateVersion.value,
                        {
                            checkUpdateVersion.value = it
                            DataStoreUtils.putSyncData(SKIP_AUTO_CHECK_UPDATE, it)
                        }
                    )
                })

            FlatButton(
                content = {
                    RowContent(
                        stringResource(id = R.string.settings_sync_config_title),
                        stringResource(id = R.string.settings_sync_config_subtitle),
                        { ResourceIcon(iconResource = R.drawable.sync) },
                        checkUpdateConfig.value,
                        {
                            checkUpdateConfig.value = it
                            DataStoreUtils.putSyncData(SKIP_AUTO_SYNC_CONFIG, it)
                        }
                    )
                })

            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.settings_permit_notice_title),
                    stringResource(
                        id = R.string.settings_permit_notice_subtitle
                    ),
                    {
                        ResourceIcon(iconResource = R.drawable.notifications)
                    },
                    permitNoticeState.value,
                    {
                        permitNoticeState.value = it
                        DataStoreUtils.putSyncData(SKIP_PERMIT_NOTICE, it)
                        if (it && !NotificationManagerCompat.from(context)
                                .areNotificationsEnabled()
                        ) {
                            val intent = Intent().apply {
                                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                putExtra(Settings.EXTRA_APP_PACKAGE, "com.android.skip")
                            }
                            context.startActivity(intent)
                        }
                    })
            })

            Menu(expandedState, options, selectedState)
            FlatButton(
                content = {
                    when (themeTypeState.value) {
                        Configuration.UI_MODE_NIGHT_NO -> RowContent(
                            options[0],
                            stringResource(id = R.string.settings_pattern_light_subtitle),
                            { ResourceIcon(iconResource = R.drawable.brightness_5) }
                        )

                        Configuration.UI_MODE_NIGHT_YES -> RowContent(
                            options[1],
                            stringResource(id = R.string.settings_pattern_dark_subtitle),
                            { ResourceIcon(iconResource = R.drawable.brightness_4) }
                        )

                        else -> RowContent(
                            options[2],
                            stringResource(id = R.string.settings_pattern_system_subtitle),
                            { ResourceIcon(iconResource = R.drawable.brightness_6) }
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

