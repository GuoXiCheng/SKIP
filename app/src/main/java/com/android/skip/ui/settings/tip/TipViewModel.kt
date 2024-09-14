package com.android.skip.ui.settings.tip

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipViewModel @Inject constructor(
    private val tipRepository: TipRepository
) : ViewModel() {
    val enable = tipRepository.enable
    fun changeEnable(enable: Boolean) = tipRepository.changeEnable(enable)
}