package com.android.skip.compose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.KeepAliveActivity
import com.android.skip.R

@Composable
fun KeepAliveButton() {
    val context = LocalContext.current

    FlatButton(
        content = {
            RowContent(stringResource(id = R.string.alive), null, R.drawable.all_inclusive)
        } ) {
        val intent = Intent(context, KeepAliveActivity::class.java)
        context.startActivity(intent)
    }
}