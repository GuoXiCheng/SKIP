package com.android.skip.compose

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.viewmodel.StartButtonViewModel


@Composable
fun StartButton(viewModel: StartButtonViewModel) {
    val state by viewModel.buttonState.collectAsState()
    val context = LocalContext.current

    ExtendedFloatingActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = {
                    Icon(
                        painter = painterResource(id = state.iconResource),
                        contentDescription = "StartButton",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(state.buttonText, color = Color.White, fontSize = 16.sp)
                }
            )
        },
        containerColor = state.backgroundColor
    )
}