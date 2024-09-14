package com.android.skip.ui.settings.strict

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StrictViewModel @Inject constructor(
    private val strictRepository: StrictRepository,
): ViewModel(){
    val enable = strictRepository.enable

    fun changeEnable(enable: Boolean) = strictRepository.changeEnable(enable)
}