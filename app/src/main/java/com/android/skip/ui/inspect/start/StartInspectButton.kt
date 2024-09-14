package com.android.skip.ui.inspect.start

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun StartInspectButton(startInspectViewModel: StartInspectViewModel) {
    val inspectState by startInspectViewModel.inspectState.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.inspect_start_title,
            subTitle = R.string.inspect_start_subtitle,
            icon = { ResourceIcon(iconResource = R.drawable.fit_screen) },
            checked = inspectState,
            {
                startInspectViewModel.changeInspectState(it)
            }
        )
    })
}