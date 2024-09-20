package com.android.skip.ui.about.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.dataclass.ConfigPostSchema
import com.android.skip.dataclass.ConfigState
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent
import com.blankj.utilcode.util.StringUtils.getString

@Composable
fun ConfigVersionButton(configViewModel: ConfigViewModel) {
    val configState = configViewModel.configPostState.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.about_config_version,
            subTitle = configState.value?.value
        )
    }, onClick = {
        configViewModel.changeConfigPostState(
            ConfigPostSchema(
                ConfigState.PENDING,
                getString(R.string.checking)
            )
        )
        configViewModel.readConfig()
    })
}