package com.android.skip.ui.inspect.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartInspectViewModel @Inject constructor(private val repository: StartInspectRepository): ViewModel() {
    val inspectState: LiveData<Boolean> = repository.inspectState

    fun changeInspectState(state: Boolean) = repository.changeInspectState(state)
}