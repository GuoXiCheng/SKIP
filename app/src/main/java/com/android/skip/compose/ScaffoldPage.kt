package com.android.skip.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.skip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldPage(
    barTitle: String,
    onClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.padding(15.dp, 15.dp, 0.dp, 0.dp),
            title = { Text(barTitle) }, navigationIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            })
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