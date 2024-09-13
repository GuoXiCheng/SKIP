package com.android.skip.ui.alive.backstage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.PictureDialog
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun BackstageButton(
    backstageViewModel:BackstageViewModel,
    onClick: ()->Unit={}
) {
    val state = backstageViewModel.showDialog.observeAsState()
    PictureDialog(showDialog = state.value == true, painter = painterResource(R.drawable.backend_lock)) {
        backstageViewModel.changeDialogState(false)
    }
    FlatButton(
        content = {
            RowContent(
                R.string.alive_backstage,
                R.string.alive_backstage_subtitle,
                { ResourceIcon(iconResource = R.drawable.counter_3) })
        }, onClick = onClick
    )
}