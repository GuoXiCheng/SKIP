package com.android.skip.ui.components.notification

import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.MyApp
import com.android.skip.R

@Composable
fun NotificationDialog(
    notificationDialogViewModel: NotificationDialogViewModel,
    onDismiss: () -> Unit
) {
    val showDialog = notificationDialogViewModel.showDialog.observeAsState()
    val context = LocalContext.current
    if (showDialog.value == true) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(text = stringResource(id = R.string.dialog_notification_title))
            },
            text = {
                Text(text = stringResource(id = R.string.dialog_notification_content))
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent().apply {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, MyApp.context.packageName)
                        }
                        context.startActivity(intent)
                        notificationDialogViewModel.changeDialogState(false)
                    }
                ) {
                    Text(stringResource(id = R.string.dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(id = R.string.dialog_dismiss))
                }
            }
        )
    }
}