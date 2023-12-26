package com.android.skip.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun ResourceIcon(iconResource: Int) {
     Icon(
        painter = painterResource(id = iconResource),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onBackground
    )
}