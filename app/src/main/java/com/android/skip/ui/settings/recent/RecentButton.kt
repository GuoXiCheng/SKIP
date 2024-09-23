package com.android.skip.ui.settings.recent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun RecentButton(recentViewModel: RecentViewModel) {
    val excludeFromRecent = recentViewModel.excludeFromRecent.observeAsState()
    FlatButton(content = {
        RowContent(
            title = R.string.settings_background_task,
            subTitle = R.string.settings_background_task_subtitle,
            icon = { ResourceIcon(iconResource = R.drawable.hide_image)},
            checked =excludeFromRecent.value,
            onCheckedChange = {
                recentViewModel.changeExcludeFromRecent(it)
            }
        )
    })
}