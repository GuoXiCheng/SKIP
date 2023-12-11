package com.android.skip

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ScaffoldPage
import com.android.skip.ui.theme.AppTheme
import com.android.skip.viewmodel.ThemeViewModel

class SettingsActivity : AppCompatActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                SettingsActivityInterface (onBackClick = { finish() },
                    onToggleTheme = {themeViewModel.toggleTheme()})
            }
        }
    }
}

@Composable
fun SettingsActivityInterface(onBackClick: () -> Unit, onToggleTheme: ()->Unit) {
    ScaffoldPage(
        barTitle = stringResource(id = R.string.settings),
        onBackClick = onBackClick, content = {
            Button(onClick = onToggleTheme) {
                Text("切换主题")
            }
        })
}


