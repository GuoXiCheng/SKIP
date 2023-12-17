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
import com.android.skip.R
import com.android.skip.SettingsActivity

@Composable
fun SettingsButton() {
    val context = LocalContext.current

    CustomFloatingButton(
        useElevation = false,
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            RowContent(
                iconResource = R.drawable.settings,
                title = stringResource(id = R.string.settings),
                subTitle = null, null
            )
        }) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }
}