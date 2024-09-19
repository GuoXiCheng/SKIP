package com.android.skip.ui.settings.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString

@Composable
fun CustomButton(configViewModel: ConfigViewModel, onClick: () -> Unit) {
    val showDialog = remember {
        mutableStateOf(false)
    }

    var text by rememberSaveable {
        mutableStateOf(
            DataStoreUtils.getSyncData(getString(R.string.store_custom_config), "")
        )
    }

    FlatButton(content = {
        RowContent(
            title = R.string.settings_custom,
            subTitle = R.string.settings_custom_subtitle,
            icon = {
                ResourceIcon(
                    iconResource = R.drawable.tune
                )
            })
    }, onClick = {
        showDialog.value = true
    })

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(shape = RoundedCornerShape(12.dp)) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 0.dp, 0.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                stringResource(id = R.string.dialog_edit_custom_config),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            IconButton(onClick = onClick) {
                                ResourceIcon(iconResource = R.drawable.info)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = text,
                                onValueChange = {
                                    text = it
                                },
                                minLines = 6,
                                textStyle = TextStyle(color = Color.Gray, fontSize = 14.sp)
                            )
                        }


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 8.dp, 8.dp, 0.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {
                                showDialog.value = false
                                if (text.isEmpty()) {
                                    DataStoreUtils.removeSync(getString(R.string.store_custom_config))
                                } else {
                                    DataStoreUtils.putSyncData(
                                        getString(R.string.store_custom_config),
                                        text
                                    )
                                }
                                configViewModel.readConfig()
                            }) {
                                Text(stringResource(id = R.string.dialog_confirm))
                            }
                        }
                    }
                }
            }
        }
    }
}