package com.android.skip.ui.inspect.floating

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun FloatingWindowButton(floatingWindowViewModel: FloatingWindowViewModel) {
    val floatingWindowState by floatingWindowViewModel.floatingWindowState.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.inspect_floating_window_title,
            subTitle = R.string.inspect_floating_window_subtitle,
            icon = { ResourceIcon(iconResource = R.drawable.float_landscape_2) },
            checked = floatingWindowState,
            {
                floatingWindowViewModel.changeFloatingWindowState(it)
            }
        )
    })
}