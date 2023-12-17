package com.android.skip.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RowContent(iconResource: Int?, title: String, subTitle: String?) {
    if (iconResource != null) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.width(16.dp))
    }
    Column {
        Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
        if (subTitle != null) {
            Text(subTitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}