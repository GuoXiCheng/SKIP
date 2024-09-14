package com.android.skip.ui.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.skip.R
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.settings.tip.TipButton
import com.android.skip.ui.settings.tip.TipViewModel
import com.android.skip.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val tipViewModel by viewModels<TipViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(R.string.settings, { finish() }, {
                    TipButton(tipViewModel)
                })
            }
        }
    }
}