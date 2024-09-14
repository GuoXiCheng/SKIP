package com.android.skip.ui.whitelist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WhiteListViewModel @Inject constructor(
    private val whiteListRepository: WhiteListRepository
) : ViewModel() {
    fun updateWhiteList(packageName: String, checked: Boolean) =
        whiteListRepository.updateWhiteList(packageName, checked)
}