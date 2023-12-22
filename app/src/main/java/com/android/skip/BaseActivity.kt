package com.android.skip

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.android.skip.manager.RectManager
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.theme.themeTypeState
import com.android.skip.utils.DataStoreUtils

const val SKIP_APP_THEME = "SKIP_APP_THEME"

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataStoreUtils.init(applicationContext)
        setContent {
            AppTheme(darkTheme = isDarkTheme()) {
                ProvideContent()
            }
        }
        RectManager.setMaxRect(this)
    }

    @Composable
    abstract fun ProvideContent()

    private fun isDarkTheme(): Boolean {
        return when (themeTypeState.value) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED ->
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            else -> false
        }
    }

}