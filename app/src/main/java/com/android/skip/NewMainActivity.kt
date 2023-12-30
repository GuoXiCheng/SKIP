package com.android.skip

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.android.skip.compose.AboutButton
import com.android.skip.compose.KeepAliveButton
import com.android.skip.compose.SettingsButton
import com.android.skip.compose.StartButton
import com.android.skip.compose.WhitelistButton
import com.android.skip.dataclass.PackageInfoV2
import com.android.skip.manager.SkipConfigManagerV2
import com.android.skip.manager.WhitelistManager
import com.android.skip.viewmodel.StartButtonViewModel
import org.yaml.snakeyaml.Yaml


class NewMainActivity : BaseActivity() {
    private val startButtonViewModel: StartButtonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val yaml = Yaml().load<List<PackageInfoV2>>(assets.open("skip_config_v2.yaml"))
            SkipConfigManagerV2.setConfig(yaml)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Composable
    override fun ProvideContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 64.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Text(
                    text = "SKIP",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            StartButton(startButtonViewModel)
            WhitelistButton()
            KeepAliveButton()
            SettingsButton()
            AboutButton()
        }
    }

    override fun onResume() {
        super.onResume()
        startButtonViewModel.changeButtonState(MyUtils.isAccessibilitySettingsOn(this))
        WhitelistManager.setWhitelist(lifecycleScope, applicationContext)
    }
}