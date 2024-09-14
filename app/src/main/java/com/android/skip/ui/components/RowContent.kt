package com.android.skip.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowContent(
    title: Any? = null,
    subTitle: Any? = null,
    icon: @Composable (() -> Unit)? = null,
    checked: Boolean? = null,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(if (checked !== null) 0.85f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(Modifier.width(16.dp))
            }
            Column {
                val titleValue = when (title) {
                    is Int -> stringResource(id = title)
                    is String -> title
                    else -> throw Error("no match type")
                }
                Text(
                    titleValue,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (subTitle != null) {
                    val subTitleValue = when (subTitle) {
                        is Int -> stringResource(id = subTitle)
                        is String -> subTitle
                        else -> throw Error("no match type")
                    }
                    Text(
                        subTitleValue,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        if (checked !== null) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                thumbContent = if (checked) {
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
