package com.android.skip.ui.main.start

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.android.skip.R
import com.android.skip.util.AccessibilityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val repository: StartAccessibilityRepository) : ViewModel() {
    private val stopped = ButtonState(Color(0xFF808080), "已停止\n点此启动", R.drawable.block)
    private val started = ButtonState(Color(0xFF1E4377), "运行中", R.drawable.check_circle)
    private val faulted = ButtonState(Color(0xFF771E1E), "发生故障\n点此重新启动", R.drawable.error)

    private val _buttonState = MutableStateFlow(stopped)
    val buttonState = _buttonState.asStateFlow()

    private val observer = Observer<AccessibilityState> {
        changeButtonState(it)
    }

    init {
        repository.accessibilityState.observeForever(observer)
    }

    private fun changeButtonState(isRunning: AccessibilityState) {
        _buttonState.value = when (isRunning) {
            AccessibilityState.STARTED -> started
            AccessibilityState.STOPPED -> stopped
            AccessibilityState.FAULTED -> faulted
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.accessibilityState.removeObserver(observer)
    }

    data class ButtonState(
        val backgroundColor: Color,
        val buttonText: String,
        val iconResource: Int
    )
}