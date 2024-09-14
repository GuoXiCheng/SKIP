package com.android.skip.ui.record.dialog

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JpegDialogViewModel @Inject constructor(
    private val jpegDialogRepository: JpegDialogRepository
) : ViewModel() {
    private val _jpegFileId = MutableLiveData<String?>()
    val jpegFileId: LiveData<String?> = _jpegFileId

    private var _bitmapJpeg: Bitmap? = null
    val bitmapJpeg
        get() = _bitmapJpeg

    fun changeJpegFileId(fileId: String?) {
        if (fileId != null) {
            _bitmapJpeg = jpegDialogRepository.getJpegByFileId(fileId)
        }
        _jpegFileId.postValue(fileId)
    }
}