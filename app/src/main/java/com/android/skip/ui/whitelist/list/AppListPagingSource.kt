package com.android.skip.ui.whitelist.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.skip.dataclass.AppListItem
import javax.inject.Inject

class AppListPagingSource @Inject constructor(
    private val appListRepository: AppListRepository,
    private val isShowSystem: Boolean
) : PagingSource<Int, AppListItem>() {
    override fun getRefreshKey(state: PagingState<Int, AppListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus((1))
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppListItem> {
        val currentPage = params.key ?: 0
        val pageSize = params.loadSize

        val pageData = appListRepository.getData(currentPage, pageSize, isShowSystem)

        return LoadResult.Page(
            data = pageData,
            prevKey = if (currentPage > 0) currentPage - 1 else null,
            nextKey = if (pageData.isEmpty()) null else currentPage + 1,
        )
    }

}