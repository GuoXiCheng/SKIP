package com.android.skip.viewmodel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.android.skip.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartButtonViewModel: ViewModel() {
    private val stopped = ButtonState(Color(0xFF808080), "已停止\n点此启动", R.drawable.block)
    private val started = ButtonState(Color(0xFF1E4377), "运行中", R.drawable.check_circle)

    private val _buttonState = MutableStateFlow(stopped)
    val buttonState = _buttonState.asStateFlow()

    fun changeButtonState(isRunning: Boolean) {
        _buttonState.value = if (isRunning) started else stopped
    }

    data class ButtonState(val backgroundColor: Color, val buttonText: String, val iconResource: Int)
}