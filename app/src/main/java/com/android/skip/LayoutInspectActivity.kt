package com.android.skip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.FlatButton
import com.android.skip.compose.ResourceIcon
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.utils.DataStoreUtils

class LayoutInspectActivity : BaseActivity() {

    @Composable
    override fun ProvideContent() {
        LayoutInspectInterface {
            finish()
        }
    }
}

@Composable
fun LayoutInspectInterface(onBackClick: () -> Unit) {

    val checkFloatingWindow = remember {
        mutableStateOf(
            DataStoreUtils.getSyncData(
                SKIP_FLOATING_WINDOW, false
            )
        )
    }

    val checkScreenCapture = remember {
        mutableStateOf(
            DataStoreUtils.getSyncData(
                SKIP_SCREEN_CAPTURE, false
            )
        )
    }

    ScaffoldPage(stringResource(id = R.string.layout_inspect), onBackClick = onBackClick, content = {
        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.inspect_screen_capture_title),
                stringResource(id = R.string.inspect_screen_capture_subtitle),
                { ResourceIcon(iconResource = R.drawable.screenshot_keyboard) },
                checkScreenCapture.value,
                {
                    checkScreenCapture.value = it
                    DataStoreUtils.putSyncData(SKIP_SCREEN_CAPTURE, it)
                }
            )
        })

        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.inspect_floating_window_title),
                stringResource(id = R.string.inspect_floating_window_subtitle),
                { ResourceIcon(iconResource = R.drawable.float_landscape_2) },
                checkFloatingWindow.value,
                {
                    checkFloatingWindow.value = it
                    DataStoreUtils.putSyncData(SKIP_FLOATING_WINDOW, it)
                }
            )
        })
    })
}