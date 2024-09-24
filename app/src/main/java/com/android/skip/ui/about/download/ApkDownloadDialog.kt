package com.android.skip.ui.about.download

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.data.download.ApkDownloadViewModel
import com.android.skip.data.version.ApkVersionViewModel
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils.getString

@Composable
fun ApkDownloadDialog(
    apkDownloadViewModel: ApkDownloadViewModel,
    apkVersionViewModel: ApkVersionViewModel
) {
    val showDialog = apkDownloadViewModel.isShowDialog.observeAsState()

    val versionPostState = apkVersionViewModel.versionPostState.observeAsState()

    val apkDownloadProcess = apkDownloadViewModel.apkDownloadProcess.observeAsState()

    if (showDialog.value == true) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(text = stringResource(id = R.string.dialog_new_version_released))
            },
            text = {
                Column {
                    Text(
                        text = getString(
                            R.string.dialog_update_version,
                            AppUtils.getAppVersionName(),
                            versionPostState.value?.latestVersion
                        )
                    )
                    apkDownloadProcess.value?.let { process ->
                        if (process > 0) {
                            Text(
                                text = getString(
                                    R.string.dialog_downloading,
                                    apkDownloadProcess.value
                                )
                            )
                        }
                    }
                }

            },
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    versionPostState.value?.latestVersion?.let {
                        apkDownloadViewModel.downloadAPK(
                            it
                        )
                    }
                }) {
                    Text(text = stringResource(id = R.string.dialog_update_now))
                }
            },
            dismissButton = {
                TextButton(onClick = { apkDownloadViewModel.changeDialogState(false) }) {
                    Text(text = stringResource(id = R.string.dialog_not_update))
                    DataStoreUtils.putSyncData(getString(R.string.store_not_update), true)
                }
            }
        )
    }
}