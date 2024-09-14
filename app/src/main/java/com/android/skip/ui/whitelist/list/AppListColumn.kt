package com.android.skip.ui.whitelist.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.whitelist.WhiteListViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppListColumn(
    appListViewModel: AppListViewModel,
    whiteListViewModel: WhiteListViewModel
) {
    val pagingDataFlow by appListViewModel.pagingData.collectAsState()

    val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { item ->
                val checked = remember { mutableStateOf(item.checked) }
                FlatButton(content = {
                    RowContent(
                        title = item.appName,
                        subTitle = item.packageName,
                        {
                            Icon(
                                painter = rememberDrawablePainter(drawable = item.appIcon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }, checked.value, {
                            checked.value = it
                            whiteListViewModel.updateWhiteList(item.packageName, it)
                        }
                    )
                })
            }
        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "加载中...")
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