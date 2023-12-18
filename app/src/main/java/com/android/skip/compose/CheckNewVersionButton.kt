package com.android.skip.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.android.skip.R

@Composable
fun CheckNewVersionButton() {
    val showDialog = remember { mutableStateOf(false) }

    FlatButton(
        content = {
            RowContent(iconResource = R.drawable.sync, title = stringResource(id = R.string.main_check_new_version), subTitle = null, null)
        }
    ) {
        showDialog.value = true
    }

    if (showDialog.value) {
        ConfirmDialog(
            title = "发现新版本",
            content = "是否立即下载更新？",
            onDismiss = { showDialog.value = false },
            onAllow = {
                showDialog.value = false
            })
    }
}
