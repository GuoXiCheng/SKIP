package com.android.skip.ui.inspect.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent

@Composable
fun InspectRecordButton(inspectRecordViewModel: InspectRecordViewModel, onClick: () -> Unit = {}) {
    val zipFileCount by inspectRecordViewModel.zipFileCount.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.inspect_record_title,
            subTitle = zipFileCount?.let {
                stringResource(
                    id = R.string.inspect_record_subtitle,
                    it
                )
            },
            icon = { ResourceIcon(iconResource = R.drawable.lists) },
        )
    }, onClick = onClick)
}