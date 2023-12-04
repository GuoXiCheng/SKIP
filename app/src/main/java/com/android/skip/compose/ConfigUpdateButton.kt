package com.android.skip.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.R

@Composable
fun ConfigUpdateButton() {
    CustomFloatingButton(
        useElevation = true,
        containerColor = Color.White,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.lists),
                contentDescription = "ConfigUpdateButton",
                tint = Color.Black
            )
            Spacer(Modifier.width(16.dp))
            Text("点此同步配置", color = Color.Black, fontSize = 16.sp)
        }) {

    }
}