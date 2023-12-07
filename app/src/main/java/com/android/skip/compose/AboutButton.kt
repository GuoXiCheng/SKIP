package com.android.skip.compose

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.AboutActivity
import com.android.skip.R

@Composable
fun AboutButton() {
    val context = LocalContext.current

    CustomFloatingButton(
        useElevation = false,
        containerColor = Color.White,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "AboutButton",
                tint = Color.Black
            )
            Spacer(Modifier.width(16.dp))
            Text("关于", fontSize = 16.sp)
    } ) {
        val intent = Intent(context, AboutActivity::class.java)
        context.startActivity(intent)
    }
}