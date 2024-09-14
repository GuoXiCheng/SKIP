package com.android.skip.ui.record.dialog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.android.skip.MyApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JpegDialogRepository @Inject constructor(){
    fun getJpegByFileId(fileId: String): Bitmap {
        val filePath = "${MyApp.context.filesDir}/capture/${fileId}.jpeg"
        val bitmap = BitmapFactory.decodeFile(filePath)
        return bitmap
    }
}