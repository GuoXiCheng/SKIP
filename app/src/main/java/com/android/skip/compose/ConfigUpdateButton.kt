package com.android.skip.compose

import androidx.compose.runtime.Composable
import com.android.skip.R

@Composable
fun ConfigUpdateButton() {
    FlatButton(
        content = {
            RowContent(iconResource = R.drawable.lists, title = "点此同步配置", subTitle = null, null)
        }) {

    }
}