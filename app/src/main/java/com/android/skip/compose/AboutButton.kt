package com.android.skip.compose

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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