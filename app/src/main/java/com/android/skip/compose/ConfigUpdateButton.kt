package com.android.skip.compose

import androidx.compose.runtime.Composable
import com.android.skip.R

@Composable
fun ConfigUpdateButton() {
    FlatButton(
        content = {
            RowContent("点此同步配置", null, R.drawable.lists)
        }) {

    }
}