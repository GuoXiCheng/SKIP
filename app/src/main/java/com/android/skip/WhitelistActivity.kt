package com.android.skip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ScaffoldPage

class WhitelistActivity : BaseActivity() {

    @Composable
    override fun ProvideContent() {
        WhitelistInterface(onBackClick = {
            finish()
        })
    }
}

@Composable
fun WhitelistInterface(onBackClick: () -> Unit) {
    ScaffoldPage(barTitle = stringResource(id = R.string.whitelist), onBackClick = onBackClick, content = {

    })
}