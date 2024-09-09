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
import com.android.skip.data.ConfigReadRepository
import com.android.skip.dataclass.ConfigReadSchema
import com.android.skip.ui.about.AboutActivity
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.inspect.InspectActivity
import com.android.skip.ui.main.start.StartAccessibilityViewModel
import com.android.skip.ui.main.start.StartButton
import com.android.skip.ui.theme.AppTheme
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import org.yaml.snakeyaml.Yaml
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val startAccessibilityViewModel by viewModels<StartAccessibilityViewModel>()

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
                    KeepAliveButton()
                    WhitelistButton()
                    InspectButton {
                        startActivity(Intent(MyApp.context, InspectActivity::class.java))
                    }
                    SettingsButton()
                    AboutButton() {
                        startActivity(Intent(MyApp.context, AboutActivity::class.java))
                    }
                }
            }
        }
    }

    @Inject
    lateinit var configReadRepository:ConfigReadRepository
    override fun onResume() {
        super.onResume()
        val yaml = Yaml().load<List<ConfigReadSchema>>(assets.open("skip_config_v3.yaml"))
        configReadRepository.readConfig(yaml)

        val map = configReadRepository.loadConfig()
        LogUtils.d(map)
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
fun KeepAliveButton() {
    FlatButton(content = {
        RowContent(R.string.alive, null, { ResourceIcon(iconResource = R.drawable.all_inclusive) })
    }) {}
}

@Composable
fun WhitelistButton() {
    FlatButton(content = {
        RowContent(R.string.whitelist,
            null,
            { ResourceIcon(iconResource = R.drawable.app_registration) })
    }) {}
}

@Composable
fun SettingsButton() {
    FlatButton(content = {
        RowContent(R.string.settings, null, { ResourceIcon(iconResource = R.drawable.settings) })
    }) {}
}

@Composable
fun AboutButton(onClick: () -> Unit = {}) {
    FlatButton(
        content = {
            RowContent(R.string.about, null, { ResourceIcon(iconResource = R.drawable.info) })
        }, onClick = onClick
    )
}