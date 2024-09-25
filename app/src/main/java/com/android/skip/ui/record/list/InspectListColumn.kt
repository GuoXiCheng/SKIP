package com.android.skip.ui.record.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.loading))
                        }
                    }
                }

                // 加载更多
                loadState.append is LoadState.Loading -> {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "加载更多...")
                        }
                    }
                }

                // 加载失败时
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

