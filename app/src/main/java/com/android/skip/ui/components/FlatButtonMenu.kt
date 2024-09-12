package com.android.skip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun FlatButtonMenu(
    content: @Composable RowScope.() -> Unit,
    menuItems: @Composable (ColumnScope.() -> Unit),
    expanded: MutableState<Boolean>
) {
    Row {
        FlatButton(content = content) {
            expanded.value = true
        }

        DropdownMenu(
            expanded = expanded.value,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            offset = DpOffset(130.dp, 0.dp),
            onDismissRequest = { expanded.value = false },
        ) {
            menuItems()
        }
    }
}
