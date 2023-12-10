package com.android.skip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ScaffoldPage
import com.android.skip.ui.theme.AppTheme

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = true) {
                SettingsActivityInterface {
                    finish()
                }
            }
        }
    }

    fun restartActivity() {
        val intent = Intent(this, this::class.java)
        finish()
        startActivity(intent)
    }

    fun setThemePreference(isDarkTheme: Boolean) {
        val sharedPreferences = getSharedPreferences("theme_preferences", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("dark_theme", isDarkTheme).apply()
    }

    fun getThemePreference(): Boolean {
        val sharedPreferences = getSharedPreferences("theme_preferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("dark_theme", false) // 默认为浅色主题
    }

}

@Composable
fun SettingsActivityInterface(onClick: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE) }
    val isDarkTheme = remember { mutableStateOf(sharedPreferences.getBoolean("dark_theme", false)) }


    ScaffoldPage(
        barTitle = stringResource(id = R.string.settings),
        onClick = onClick, content = {
            Button(onClick = {
                isDarkTheme.value = !isDarkTheme.value
                sharedPreferences.edit().putBoolean("dark_theme", isDarkTheme.value).apply()
            }) {
                Text("切换主题")
            }
        })
}


