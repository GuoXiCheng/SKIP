package com.android.skip.ui.about.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent

@Composable
fun ConfigVersionButton(configViewModel: ConfigViewModel) {
    val configVersion = configViewModel.configHashCode.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.about_config_version,
            subTitle = configVersion.value
        )
    })
}