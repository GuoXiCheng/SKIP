package com.android.skip.ui.inspect

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.service.InspectService
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.inspect.record.InspectRecordButton
import com.android.skip.ui.inspect.record.InspectRecordViewModel
import com.android.skip.ui.inspect.start.StartInspectButton
import com.android.skip.ui.inspect.start.StartInspectViewModel
import com.android.skip.ui.record.InspectRecordActivity
import com.android.skip.ui.theme.AppTheme
import com.blankj.utilcode.util.ServiceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectActivity : AppCompatActivity() {
    private val startInspectViewModel by viewModels<StartInspectViewModel>()

    private val inspectRecordViewModel by viewModels<InspectRecordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(R.string.inspect, { finish() }) {
                    StartInspectButton(startInspectViewModel)
                    InspectRecordButton(inspectRecordViewModel) {
                        startActivity(Intent(MyApp.context, InspectRecordActivity::class.java))
                    }
                }
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
                        // TODO(通知权限检查)
                        val mediaProjectionManager =
                            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                        val screenCaptureIntent = mediaProjectionManager.createScreenCaptureIntent()
                        screenshotPermissionLauncher.launch(screenCaptureIntent)
                    }
                }

                false -> {
                    if (ServiceUtils.isServiceRunning(InspectService::class.java)) {
                        ServiceUtils.stopService(InspectService::class.java)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}