package com.android.skip.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import com.android.skip.R
import com.blankj.utilcode.util.ScreenUtils

@Composable
fun PictureDialog(
    showDialog: Boolean,
    painter: Painter?,
    onClick: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (painter != null) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.heightIn(max = Dp(ScreenUtils.getScreenYDpi() * 2))
                        )
                    }
                    TextButton(onClick = onClick) {
                        Text(stringResource(id = R.string.dialog_close))
                    }
                }
            }
        }
    }
}