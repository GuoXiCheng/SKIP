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
            title = "是否启用布局检查",
            subTitle = "启用布局检查记录节点信息",
            icon = { ResourceIcon(iconResource = R.drawable.fit_screen) },
            checked = inspectState,
            {
                startInspectViewModel.changeInspectState(it)
            }
        )
    })
}