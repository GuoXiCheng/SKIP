package com.android.skip.ui.whitelist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.skip.dataclass.AppListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appListRepository: AppListRepository
): ViewModel() {
    private val _pagingData = MutableStateFlow(createPager(false).flow.cachedIn(viewModelScope))
    val pagingData: StateFlow<Flow<PagingData<AppListItem>>> = _pagingData

    private var currentPagingSource: AppListPagingSource? = null

    fun reloadData(isShowSystem: Boolean) {
        currentPagingSource?.invalidate()
        _pagingData.value = createPager(isShowSystem).flow.cachedIn(viewModelScope)
    }

    private fun createPager(isShowSystem: Boolean): Pager<Int, AppListItem> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val newPagingSource = AppListPagingSource(appListRepository, isShowSystem)
                currentPagingSource = newPagingSource
                newPagingSource
            }
        )
    }
}