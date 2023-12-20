package com.android.skip

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.FlatButton
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage

class KeepAliveActivity : BaseActivity() {

    @Composable
    override fun ProvideContent() {
        KeepAliveInterface() {
            finish()
        }
    }
}

@Composable
fun KeepAliveInterface(onBackClick: () -> Unit) {
    ScaffoldPage(stringResource(id = R.string.alive), onBackClick = onBackClick, content = {
        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.alive_power_saving_title),
                stringResource(id = R.string.alive_power_saving_subtitle),
                R.drawable.counter_1
            )
        })
        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.alive_self_start_title),
                stringResource(id = R.string.alive_self_start_subtitle),
                R.drawable.counter_2
            )
        })
        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.alive_backstage_title),
                stringResource(id = R.string.alive_backstage_subtitle),
                R.drawable.counter_3
            )
        })
        FlatButton(content = {
            RowContent(
                stringResource(id = R.string.alive_warn_title),
                stringResource(id = R.string.alive_warn_subtitle),
                R.drawable.warning
            )
        })
    })
}