package com.android.skip.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.R

@Composable
fun CheckNewVersionButton() {
    val showDialog = remember { mutableStateOf(false) }

    CustomFloatingButton(
        useElevation = false,
        containerColor = MaterialTheme.colorScheme.background,
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
