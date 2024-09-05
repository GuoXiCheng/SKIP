package com.android.skip.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.R

@Composable
fun ScaffoldPage(
    barTitle: Int,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(topBar = {
        Row(modifier = Modifier.padding(15.dp, 15.dp, 0.dp, 0.dp)) {
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