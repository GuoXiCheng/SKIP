package com.android.skip.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.settings.custom.CustomButton
import com.android.skip.ui.settings.strict.StrictButton
import com.android.skip.ui.settings.strict.StrictViewModel
import com.android.skip.ui.settings.tip.TipButton
import com.android.skip.ui.settings.tip.TipViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val tipViewModel by viewModels<TipViewModel>()

    private val strictViewModel by viewModels<StrictViewModel>()

    private val configViewModel by viewModels<ConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ScaffoldPage(R.string.settings, { finish() }, {
                    TipButton(tipViewModel)
                    StrictButton(strictViewModel)
                    CustomButton(configViewModel) {
                        val intent = Intent(this, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.settings_custom_config_url)
                        }
                        startActivity(intent)
                    }
                })
            }
        }
    }
}