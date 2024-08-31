package com.android.skip.ui.main.start

import android.provider.Settings
import android.text.TextUtils
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.service.MyAccessibilityService
import com.blankj.utilcode.util.ServiceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {
    private val stopped = ButtonState(Color(0xFF808080), "已停止\n点此启动", R.drawable.block)
    private val started = ButtonState(Color(0xFF1E4377), "运行中", R.drawable.check_circle)
    private val faulted = ButtonState(Color(0xFF771E1E), "发生故障\n点此重新启动", R.drawable.error)

    private val _buttonState = MutableStateFlow(stopped)
    val buttonState = _buttonState.asStateFlow()

    fun changeButtonState(isRunning: AccessibilityState) {
        _buttonState.value = when (isRunning) {
            AccessibilityState.STARTED -> started
            AccessibilityState.STOPPED -> stopped
            AccessibilityState.FAULTED -> faulted
        }
    }

    fun getAccessibilityState(): AccessibilityState {
        val isSettingOn = isAccessibilitySettingsOn()
        val isRunning = isAccessibilitySettingsRunning()

        return when {
            isSettingOn && isRunning -> AccessibilityState.STARTED
            isSettingOn && !isRunning -> AccessibilityState.FAULTED
            else -> AccessibilityState.STOPPED
        }
    }

    /**
     * 判断无障碍服务是否：启用
     */
    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val context = MyApp.context
        val service = context.packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        val simpleStringSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingsValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingsValue != null) {
                simpleStringSplitter.setString(settingsValue)
                while (simpleStringSplitter.hasNext()) {
                    val accessibilityService = simpleStringSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) return true
                }
            }
        }
        return false
    }

    /**
     * 判断无障碍服务是否：可用
     */
    private fun isAccessibilitySettingsRunning(): Boolean {
        return ServiceUtils.isServiceRunning(MyAccessibilityService::class.java)
    }

    data class ButtonState(
        val backgroundColor: Color,
        val buttonText: String,
        val iconResource: Int
    )
}