package com.android.skip.ui.record.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.skip.dataclass.InspectRecordItem
import com.blankj.utilcode.util.LogUtils
import javax.inject.Inject


class ZipFilePagingSource @Inject constructor(private val inspectListRepository: InspectListRepository) :
    PagingSource<Int, InspectRecordItem>() {
    override fun getRefreshKey(state: PagingState<Int, InspectRecordItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus((1))
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InspectRecordItem> {
        return try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize

            val pageData = inspectListRepository.getData(currentPage, pageSize)

            val fromIndex = currentPage * pageSize
            val toIndex = minOf(fromIndex + pageSize, pageData.size)

            LoadResult.Page(
                data = pageData,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (toIndex == pageData.size) null else currentPage + 1
            )
        } catch (e: Exception) {
            LogUtils.e(e)
            LoadResult.Error(e)
        }
    }

}