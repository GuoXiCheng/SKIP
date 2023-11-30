package com.android.skip.compose

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.skip.viewmodel.StartButtonViewModel


@Composable
fun StartButton(viewModel: StartButtonViewModel) {
    //    val context = LocalContext.current
    val state by viewModel.buttonState.collectAsState()

    ExtendedFloatingActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        text = { StartButtonContent(iconResource = state.iconResource, buttonText = state.buttonText) },
        onClick = {

        },
        icon = {},
        containerColor = state.backgroundColor // 使用背景色状态
    )
}

@Composable
private fun StartButtonContent(iconResource: Int, buttonText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = "Block",
            tint = Color.White
        )
        Spacer(Modifier.width(16.dp)) // 图标和文本之间的间距
        Text(buttonText, color = Color.White)
    }
}