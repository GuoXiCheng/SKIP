package com.android.skip.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

typealias ButtonContent = @Composable RowScope.() -> Unit

@Composable
fun CustomFloatingButton(
    useElevation: Boolean,
    containerColor: Color,
    content: ButtonContent,
    onClick: ()->Unit) {
    val elevation = if (useElevation) {
        FloatingActionButtonDefaults.elevation() // 使用默认阴影
    } else {
        flatElevation() // 使用扁平化效果，即没有阴影
    }

    val buttonHeight = if (useElevation) 80.dp else 60.dp

    ExtendedFloatingActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        onClick = onClick,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = content
            )
        },
        elevation = elevation,
        containerColor = containerColor
    )
}

@Composable
private fun flatElevation(): FloatingActionButtonElevation {
    return FloatingActionButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )
}