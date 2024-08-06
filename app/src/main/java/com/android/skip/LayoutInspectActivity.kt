package com.android.skip

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.android.skip.compose.FlatButton
import com.android.skip.compose.ResourceIcon
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.service.LayoutInspectService
import com.android.skip.utils.DataStoreUtils

class LayoutInspectActivity : BaseActivity() {
    private lateinit var screenshotPermissionLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenshotPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, LayoutInspectService::class.java).apply {
                    putExtra("resultCode", result.resultCode)
                    putExtra("data", result.data)
                }
                ContextCompat.startForegroundService(this, intent)
            }
        }
    }

    @Composable
    override fun ProvideContent() {
        LayoutInspectInterface {
            finish()
        }

    }

    @Composable
    fun LayoutInspectInterface(onBackClick: () -> Unit) {
        val context = LocalContext.current

        val checkLayoutInspect = remember {
            mutableStateOf(
                DataStoreUtils.getSyncData(
                    SKIP_LAYOUT_INSPECT, false
                )
            )
        }

        ScaffoldPage(
            stringResource(id = R.string.layout_inspect),
            onBackClick = onBackClick,
            content = {
                FlatButton(content = {
                    RowContent(stringResource(id = R.string.layout_inspect_title),
                        stringResource(id = R.string.layout_inspect_subtitle),
                        { ResourceIcon(iconResource = R.drawable.fit_screen) },
                        checkLayoutInspect.value,
                        {
                            checkLayoutInspect.value = it
                            DataStoreUtils.putSyncData(SKIP_LAYOUT_INSPECT, it)
                            if (checkLayoutInspect.value) {
                                val mediaProjectionManager =
                                    getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

                                val captureIntent =
                                    mediaProjectionManager.createScreenCaptureIntent()
                                screenshotPermissionLauncher.launch(captureIntent)
                            } else {
                                val intent = Intent(context, LayoutInspectService::class.java)
                                stopService(intent)
                            }
                        })
                })
            })
    }
}

