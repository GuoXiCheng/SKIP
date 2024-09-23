package com.android.skip.ui.settings.strict

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun StrictButton(
    strictViewModel: StrictViewModel
) {
    val enable = strictViewModel.enable.observeAsState()
    val strictText = if (enable.value == true) {
        R.string.settings_strict_yes
    } else {
        R.string.settings_strict_no
    }

    FlatButton(
        content = {
            RowContent(
                R.string.settings_strict,
                stringResource(id = strictText),
                { ResourceIcon(iconResource = R.drawable.target) },
                enable.value, {
                    strictViewModel.changeEnable(it)
                })
        }, onClick = {}
    )
}