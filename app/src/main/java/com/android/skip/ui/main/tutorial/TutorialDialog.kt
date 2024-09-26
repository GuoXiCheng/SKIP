package com.android.skip.ui.main.tutorial

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.android.skip.R

@Composable
fun TutorialDialog(
    tutorialViewModel: TutorialViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isShowDialog = tutorialViewModel.isShowDialog.observeAsState()
    if (isShowDialog.value == true) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(text = stringResource(id = R.string.dialog_get_started))
            },
            text = {
                Text(text = stringResource(id = R.string.dialog_get_started_content))
            },
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = onConfirm
                ) {
                    Text(stringResource(id = R.string.dialog_go_at_once))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(id = R.string.dialog_no_more_reminders))
                }
            }
        )
    }
}