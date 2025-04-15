package com.android.skip.ui.inspect

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.service.AccessibilityInspectViewModel
import com.android.skip.service.FloatingWindowService
import com.android.skip.service.InspectService
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.components.expandMenuItems
import com.android.skip.ui.components.notification.NotificationDialog
import com.android.skip.ui.components.notification.NotificationDialogViewModel
import com.android.skip.ui.inspect.floating.FloatingWindowButton
import com.android.skip.ui.inspect.floating.FloatingWindowViewModel
import com.android.skip.ui.inspect.record.InspectRecordButton
import com.android.skip.ui.inspect.record.InspectRecordViewModel
import com.android.skip.ui.inspect.start.StartInspectButton
import com.android.skip.ui.inspect.start.StartInspectViewModel
import com.android.skip.ui.record.InspectRecordActivity
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import com.blankj.utilcode.util.ServiceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectActivity : AppCompatActivity() {
    private val startInspectViewModel by viewModels<StartInspectViewModel>()

    private val inspectRecordViewModel by viewModels<InspectRecordViewModel>()

    private val accessibilityInspectViewModel by viewModels<AccessibilityInspectViewModel>()

    private val notificationDialogViewModel by viewModels<NotificationDialogViewModel>()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    private val floatingWindowViewModel by viewModels<FloatingWindowViewModel>()

    private val requestOverlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this, FloatingWindowService::class.java))
        }
    }

    private fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            requestOverlayPermissionLauncher.launch(intent)
        } else {
            startService(Intent(this, FloatingWindowService::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.inspect, { finish() }, {
                    FloatingWindowButton(floatingWindowViewModel)
                    StartInspectButton(startInspectViewModel)
                    InspectRecordButton(inspectRecordViewModel) {
                        startActivity(Intent(MyApp.context, InspectRecordActivity::class.java))
                    }
                    NotificationDialog(notificationDialogViewModel) {
                        notificationDialogViewModel.changeDialogState(false)
                        startInspectViewModel.changeInspectState(false)
                    }
                },{
                    DropdownMenuItem(
                        leadingIcon = { ResourceIcon(iconResource = R.drawable.help) },
                        text = { Text(stringResource(id = R.string.inspect_function_intro)) },
                        onClick = {
                            val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                                putExtra("url", R.string.inspect_function_intro_url)
                            }
                            startActivity(intent)
                            expandMenuItems = false
                        })
                })
            }
        }

        val screenshotPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = Intent(this, InspectService::class.java).apply {
                        putExtra("result_code", result.resultCode)
                        putExtra("result_data", result.data)
                    }
                    ContextCompat.startForegroundService(this, intent)
                } else {
                    startInspectViewModel.changeInspectState(false)
                }
            }

        startInspectViewModel.inspectState.observe(this) { value ->
            when (value) {
                true -> {
                    if (!ServiceUtils.isServiceRunning(InspectService::class.java)) {

                        if (!NotificationManagerCompat.from(MyApp.context).areNotificationsEnabled()) {
                            notificationDialogViewModel.changeDialogState(true)
                        } else {
                            val mediaProjectionManager =
                                getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                            val screenCaptureIntent =
                                mediaProjectionManager.createScreenCaptureIntent()
                            screenshotPermissionLauncher.launch(screenCaptureIntent)
                        }
                    }
                }

                false -> {
                    if (ServiceUtils.isServiceRunning(InspectService::class.java)) {
                        ServiceUtils.stopService(InspectService::class.java)
                    }
                }
            }
        }

        accessibilityInspectViewModel.accessibilityInspectSuccess.observe(this) {
            inspectRecordViewModel.changeZipFileCount()
        }

        floatingWindowViewModel.floatingWindowState.observe(this) { value ->
            when (value) {
                true -> {
                    if (!ServiceUtils.isServiceRunning(FloatingWindowService::class.java)) {
                        requestOverlayPermission()
                    }
                }
                false -> {
                    if (ServiceUtils.isServiceRunning(FloatingWindowService::class.java)) {
                        ServiceUtils.stopService(FloatingWindowService::class.java)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!ServiceUtils.isServiceRunning(InspectService::class.java)) {
            startInspectViewModel.changeInspectState(false)
        }
        if (!ServiceUtils.isServiceRunning(FloatingWindowService::class.java)) {
            floatingWindowViewModel.changeFloatingWindowState(false)
        }
    }
}