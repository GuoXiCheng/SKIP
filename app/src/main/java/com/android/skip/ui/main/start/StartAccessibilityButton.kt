package com.android.skip.ui.main.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.sp

@Composable
fun StartButton(startAccessibilityViewModel: StartAccessibilityViewModel, onClick: () -> Unit = {}) {
    val buttonState by startAccessibilityViewModel.buttonState.collectAsState()

    ExtendedFloatingActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=80.dp),
        onClick = onClick,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = buttonState.iconResource),
                    tint = Color.White,
                    contentDescription = "StartButton"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(buttonState.buttonText, color = Color.White, fontSize = 16.sp)
            }
        },
        containerColor = buttonState.backgroundColor
    )
}