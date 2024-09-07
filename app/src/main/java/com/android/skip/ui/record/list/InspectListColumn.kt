package com.android.skip.ui.record.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.skip.ui.components.FlatButtonMenu
import com.android.skip.ui.components.RowContent
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun InspectListColumn(inspectListViewModel: InspectListViewModel) {
    // 获取分页数据流
    val lazyPagingItems = inspectListViewModel.filePagingData.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let {
                FlatButtonMenu(content = {
                    RowContent(
                        title = it.appName,
                        subTitle = it.activityName,
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
                        text = { Text(text = "发送") },
                        onClick = { /*TODO*/ }
                    )
                })
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

