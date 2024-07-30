package com.android.skip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ScaffoldPage

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
    ScaffoldPage(stringResource(id = R.string.layout_inspect), onBackClick = onBackClick, content = {

    })
}