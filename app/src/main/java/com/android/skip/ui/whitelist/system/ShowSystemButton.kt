package com.android.skip.ui.whitelist.system

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.skip.R

@Composable
fun ShowSystemButton(showSystemViewModel: ShowSystemViewModel) {
    val isShowSystem = showSystemViewModel.isShowSystem.observeAsState()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isShowSystem.value == true,
            onCheckedChange = {
                showSystemViewModel.changeIsShowSystem(it)
            }
        )
        Text(stringResource(id = R.string.whitelist_show_system))
        Spacer(modifier = Modifier.width(10.dp))
    }
}