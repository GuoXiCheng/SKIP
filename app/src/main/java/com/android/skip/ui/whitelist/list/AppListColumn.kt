package com.android.skip.ui.whitelist.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.whitelist.WhiteListViewModel

@OptIn(ExperimentalCoilApi::class)
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
                            Image(
                                painter = rememberImagePainter(data = item.appIcon),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
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