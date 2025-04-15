package com.android.skip.ui.inspect.floating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FloatingWindowViewModel @Inject constructor(private val repository: FloatingWindowRepository): ViewModel() {
    val floatingWindowState: LiveData<Boolean> = repository.floatingWindowState

    fun changeFloatingWindowState(state: Boolean) = repository.changeFloatingWindowState(state)
}