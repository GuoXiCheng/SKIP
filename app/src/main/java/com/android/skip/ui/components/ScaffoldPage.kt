package com.android.skip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.R

var expandMenuItems by mutableStateOf(false)

@Composable
fun ScaffoldPage(
    barTitle: Int,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    menuItems: (@Composable (ColumnScope.() -> Unit))? = null
) {
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(
                    15.dp, 15.dp, 15.dp, 0.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    ResourceIcon(iconResource = R.drawable.arrow_back)
                }
                Text(
                    text = stringResource(id = barTitle),
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            menuItems?.let {
                Row {
                    IconButton(onClick = { expandMenuItems = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                        DropdownMenu(expanded = expandMenuItems,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            onDismissRequest = { expandMenuItems = false }) {
                            menuItems()
                        }
                    }
                }
            }

        }
    }, containerColor = MaterialTheme.colorScheme.background,
        content = { contentPadding ->
            Column(
                Modifier
                    .padding(contentPadding)
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                content(contentPadding)
            }
        })
}