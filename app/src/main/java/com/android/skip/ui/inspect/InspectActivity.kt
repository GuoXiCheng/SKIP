package com.android.skip.ui.inspect

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.android.skip.R
import com.android.skip.service.InspectService
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.inspect.start.StartInspectButton
import com.android.skip.ui.inspect.start.StartInspectViewModel
import com.android.skip.ui.theme.AppTheme
import com.blankj.utilcode.util.ServiceUtils

class InspectActivity : AppCompatActivity() {
    private val startInspectViewModel by viewModels<StartInspectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(stringResource(id = R.string.layout_inspect), { finish() }) {
                    StartInspectButton(startInspectViewModel)
                }
            }
        }

        val screenshotPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = Intent(this, InspectService::class.java).apply {
                        putExtra("resultCode", result.resultCode)
                        putExtra("data", result.data)
                    }
                    ContextCompat.startForegroundService(this, intent)
                } else {
                    startInspectViewModel.changeInspectState(false)
                }
            }

        startInspectViewModel.inspectState.observe(this) { value ->
            when (value) {
                true -> {
                    // TODO(通知权限检查)
                    val mediaProjectionManager =
                        getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                    val screenCaptureIntent = mediaProjectionManager.createScreenCaptureIntent()
                    screenshotPermissionLauncher.launch(screenCaptureIntent)
                }

                false -> {
                    if (ServiceUtils.isServiceRunning(InspectService::class.java)) {
                        ServiceUtils.stopService(InspectService::class.java)
                    }
                }
            }
        }
    }
}