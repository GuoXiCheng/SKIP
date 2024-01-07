package com.android.skip.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.R

@Composable
fun OpenBrowserDialog(openName: String, openUrl: String, showDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    if (showDialog.value) {
        ConfirmDialog(
            title = stringResource(id = R.string.dialog_open_browser_title),
            content = stringResource(id = R.string.dialog_open_browser_content) + openName,
            onDismiss = { showDialog.value = false },
            onAllow = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(openUrl))
                context.startActivity(intent)
                showDialog.value = false
            })
    }
}