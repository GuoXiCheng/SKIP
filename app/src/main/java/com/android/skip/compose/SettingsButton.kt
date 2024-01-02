package com.android.skip.compose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.SettingsActivity

@Composable
fun SettingsButton() {
    val context = LocalContext.current

    FlatButton(
        content = {
            RowContent(
                stringResource(id = R.string.settings),
                null,
                { ResourceIcon(iconResource = R.drawable.settings) }
            )
        }) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }
}