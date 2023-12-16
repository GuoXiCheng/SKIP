package com.android.skip

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ScaffoldPage
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.theme.themeTypeState
import com.android.skip.utils.DataStoreUtils

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = themeTypeState.value == Configuration.UI_MODE_NIGHT_YES) {
                SettingsActivityInterface (onBackClick = { finish() })
            }
        }
    }
}

@Composable
fun SettingsActivityInterface(onBackClick: () -> Unit) {
    ScaffoldPage(
        barTitle = stringResource(id = R.string.settings),
        onBackClick = onBackClick, content = {
            Button(onClick = { toggleTheme() }) {
                Text(text = if (themeTypeState.value == Configuration.UI_MODE_NIGHT_YES) "浅色主题" else "深色主题")
            }
        })
}

private fun toggleTheme() {
    val newTheme = if (themeTypeState.value == Configuration.UI_MODE_NIGHT_YES) {
        Configuration.UI_MODE_NIGHT_NO
    } else {
        Configuration.UI_MODE_NIGHT_YES
    }

    themeTypeState.value = newTheme
    DataStoreUtils.putSyncData(SKIP_APP_THEME, newTheme)
}

