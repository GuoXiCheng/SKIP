package com.android.skip.ui.alive.notificationbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun NotificationBarButton(
    notificationBarViewModel: NotificationBarViewModel,
    onClick: () -> Unit = {}
) {
    val enable = notificationBarViewModel.enable.observeAsState()
    FlatButton(
        content = {
            RowContent(
                R.string.alive_notification_bar,
                R.string.alive_notification_bar_subtitle,
                { ResourceIcon(iconResource = R.drawable.counter_4) },
                enable.value, {
                    notificationBarViewModel.changeEnable(it)
                })
        }, onClick = onClick
    )
}