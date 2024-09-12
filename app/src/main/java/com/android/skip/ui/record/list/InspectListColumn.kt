package com.android.skip.ui.record.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.skip.R
import com.android.skip.ui.components.FlatButtonMenu
import com.android.skip.ui.components.RowContent
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun InspectListColumn(
    inspectListViewModel: InspectListViewModel,
    onClick: (fileId: String, menuType: String) -> Unit
) {
    // 获取分页数据流
    val lazyPagingItems = inspectListViewModel.filePagingData.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let {
                val expandedState = remember { mutableStateOf(false) }
                FlatButtonMenu(content = {
                    RowContent(
                        title = it.appName,
                        subTitle = it.fileId,
                        {
                            Icon(
                                painter = rememberDrawablePainter(drawable = it.appIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }, menuItems = {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.record_look)) },
                        leadingIcon = {Icon(Icons.Outlined.Info, contentDescription = null)},
                        onClick = {
                            onClick(it.fileId, R.string.record_look.toString())
                            expandedState.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.record_share)) },
                        leadingIcon = {Icon(Icons.Outlined.Share, contentDescription = null)},
                        onClick = {
                            onClick(it.fileId, R.string.record_share.toString())
                            expandedState.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.record_delete)) },
                        leadingIcon = {Icon(Icons.Outlined.Delete, contentDescription = null)},
                        onClick = {
                            onClick(it.fileId, R.string.record_delete.toString())
                            expandedState.value = false
                        })
                }, expandedState)
            }
        }

        // 添加一个加载状态的 Footer
        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() } // 加载中状态
                }

                loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() } // 上拉加载更多状态
                }

                loadState.append is LoadState.Error -> {
                    item {
                        val e = lazyPagingItems.loadState.append as LoadState.Error
                        Text(text = "加载失败: ${e.error.message}")
                    }
                }
            }
        }
    }
}

