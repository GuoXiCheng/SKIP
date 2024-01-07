package com.android.skip.compose

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.skip.R
import com.android.skip.themeTypeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit,
    onAllow: () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    Dialog(onDismissRequest = { /* 点击外部不关闭对话框 */ }) {
        Card(
            modifier = Modifier.height(200.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    )
                    Text(
                        text = content,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = if (darkTheme || themeTypeState.value == Configuration.UI_MODE_NIGHT_YES) ButtonDefaults.buttonColors(
                            Color(0xFF454545)
                        ) else ButtonDefaults.buttonColors(Color(0xFFF0F0F0))
                    ) {
                        Text(
                            text = stringResource(id = R.string.dialog_confirm_dismiss),
                            color = Color(0xFFc3c3c3),
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onAllow,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF0073dd)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.dialog_confirm_allow),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

