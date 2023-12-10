package com.android.skip.compose

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
import com.android.skip.R

@Composable
fun ConfigUpdateButton() {
    CustomFloatingButton(
        useElevation = true,
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.lists),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.width(16.dp))
            Text("点此同步配置", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
        }) {

    }
}