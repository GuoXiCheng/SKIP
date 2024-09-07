package com.android.skip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ScreenUtils

@Composable
fun FlatButtonMenu(
    content: @Composable RowScope.() -> Unit,
    menuItems: @Composable (ColumnScope.() -> Unit)
) {
    var expanded by remember { mutableStateOf(false) }

    Row {
        FlatButton(content = content) {
            expanded = true
        }

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            offset = DpOffset((ScreenUtils.getScreenXDpi() / 3).dp, 0.dp),
            onDismissRequest = { expanded = false },
        ) {
            menuItems()
        }
    }
}
