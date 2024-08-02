package com.android.skip.compose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.LayoutInspectActivity
import com.android.skip.R

@Composable
fun LayoutInspectButton() {
    val context = LocalContext.current

    FlatButton(
        content = {
            RowContent(
                stringResource(id = R.string.layout_inspect),
                null,
                { ResourceIcon(iconResource = R.drawable.fit_screen) })
        }) {
        val intent = Intent(context, LayoutInspectActivity::class.java)
        context.startActivity(intent)
    }
}