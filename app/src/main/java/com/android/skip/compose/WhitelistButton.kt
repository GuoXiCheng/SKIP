package com.android.skip.compose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.R
import com.android.skip.WhitelistActivity

@Composable
fun WhitelistButton() {
    val context = LocalContext.current

    FlatButton(
        content = {
            RowContent(
                stringResource(id = R.string.whitelist), null, R.drawable.app_registration
            )
        }) {
        val intent = Intent(context, WhitelistActivity::class.java)
        context.startActivity(intent)
    }
}