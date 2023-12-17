package com.android.skip.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RowContent(iconResource: Int?, title: String, subTitle: String?, checked: MutableState<Boolean>?) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconResource != null) {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(16.dp))
            }
            Column {
                Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                if (subTitle != null) {
                    Text(subTitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        if (checked !== null) {
            Switch(
                checked = checked.value,
                onCheckedChange = { checked.value = it },
                thumbContent = if (checked.value) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else null,
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}
