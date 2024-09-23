package com.android.skip.ui.settings.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun AutoUpdateButton(autoUpdateViewModel: AutoUpdateViewModel) {
    val autoUpdate = autoUpdateViewModel.autoUpdate.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.settings_auto_update,
            subTitle = R.string.settings_auto_update_subtitle,
            icon = { ResourceIcon(iconResource = R.drawable.sync)},
            checked = autoUpdate.value,
            {
                autoUpdateViewModel.changeAutoUpdate(it)
            }
        )
    })
}