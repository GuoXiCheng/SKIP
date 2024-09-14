package com.android.skip.ui.settings.tip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun TipButton(tipViewModel: TipViewModel) {
    val enable = tipViewModel.enable.observeAsState()
    val context = LocalContext.current
    val isShowText =
        if (enable.value == true)
            context.getString(R.string.settings_tip_yes)
        else
            context.getString(R.string.settings_tip_no)

    FlatButton(
        content = {
            RowContent(
                R.string.settings_tip,
                stringResource(id = R.string.settings_tip_subtitle, isShowText),
                { ResourceIcon(iconResource = R.drawable.notifications) },
                enable.value, {
                    tipViewModel.changeEnable(it)
                })
        }, onClick = {}
    )
}