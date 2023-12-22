package com.android.skip.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.android.skip.R

@Composable
fun PictureDialog(
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.backend_lock),
                        contentDescription = "Dialog Image"
                    )
                    TextButton(
                        onClick = { showDialog.value = false }
                    ) {
                        Text("我知道了")
                    }
                }
            }
        }
    }
}
