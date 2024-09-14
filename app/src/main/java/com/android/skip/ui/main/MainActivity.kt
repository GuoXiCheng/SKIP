package com.android.skip.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.ui.about.AboutActivity
import com.android.skip.ui.alive.AliveActivity
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.inspect.InspectActivity
import com.android.skip.ui.main.start.StartAccessibilityViewModel
import com.android.skip.ui.main.start.StartButton
import com.android.skip.ui.settings.SettingsActivity
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.whitelist.WhiteListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val startAccessibilityViewModel by viewModels<StartAccessibilityViewModel>()

    private val configViewModel by viewModels<ConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 64.dp, horizontal = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppTitle()
                    StartButton(startAccessibilityViewModel = startAccessibilityViewModel) {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                    KeepAliveButton {
                        startActivity(Intent(MyApp.context, AliveActivity::class.java))
                    }
                    WhiteListButton {
                        startActivity(Intent(MyApp.context, WhiteListActivity::class.java))
                    }
                    InspectButton {
                        startActivity(Intent(MyApp.context, InspectActivity::class.java))
                    }
                    SettingsButton {
                        startActivity(Intent(MyApp.context, SettingsActivity::class.java))
                    }
                    AboutButton() {
                        startActivity(Intent(MyApp.context, AboutActivity::class.java))
                    }
                }
            }
        }

        configViewModel.readConfig(this)
    }

    override fun onResume() {
        super.onResume()
    }
}

@Composable
fun AppTitle() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun InspectButton(onClick: () -> Unit = {}) {
    FlatButton(
        content = {
            RowContent(R.string.inspect,
                null,
                { ResourceIcon(iconResource = R.drawable.fit_screen) })
        }, onClick = onClick
    )
}

@Composable
fun KeepAliveButton(onClick: () -> Unit = {}) {
    FlatButton(content = {
        RowContent(R.string.alive, null, { ResourceIcon(iconResource = R.drawable.all_inclusive) })
    }, onClick = onClick)
}

@Composable
fun WhiteListButton(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(R.string.whitelist,
            null,
            { ResourceIcon(iconResource = R.drawable.app_registration) })
    }, onClick = onClick)
}

@Composable
fun SettingsButton(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(R.string.settings, null, { ResourceIcon(iconResource = R.drawable.settings) })
    }, onClick = onClick)
}

@Composable
fun AboutButton(onClick: () -> Unit = {}) {
    FlatButton(
        content = {
            RowContent(R.string.about, null, { ResourceIcon(iconResource = R.drawable.info) })
        }, onClick = onClick
    )
}