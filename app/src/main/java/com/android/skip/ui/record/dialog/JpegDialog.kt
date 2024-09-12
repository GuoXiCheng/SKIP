package com.android.skip.ui.record.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.android.skip.ui.components.PictureDialog

@Composable
fun JpegDialog(jpegDialogViewModel: JpegDialogViewModel) {
    val jpegFileIdState = jpegDialogViewModel.jpegFileId.observeAsState()
    PictureDialog(
        showDialog = jpegFileIdState.value != null,
        painter = jpegDialogViewModel.bitmapJpeg?.let { BitmapPainter(it.asImageBitmap()) }
    ) {
        jpegDialogViewModel.changeJpegFileId(null)
    }
}