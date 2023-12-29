package com.android.skip.compose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.AboutActivity
import com.android.skip.R

@Composable
fun AboutButton() {
    val context = LocalContext.current

    FlatButton(
        content = {
            RowContent(
                stringResource(id = R.string.about),
                null,
                { ResourceIcon(iconResource = R.drawable.info) })
        }) {
        val intent = Intent(context, AboutActivity::class.java)
        context.startActivity(intent)
    }
}