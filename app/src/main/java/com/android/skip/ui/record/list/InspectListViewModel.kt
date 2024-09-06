package com.android.skip.ui.record.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.skip.dataclass.InspectRecordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class InspectListViewModel @Inject constructor(private val zipFilePagingSource: ZipFilePagingSource) :
    ViewModel() {

    val filePagingData: Flow<PagingData<InspectRecordItem>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { zipFilePagingSource }
    ).flow.cachedIn(viewModelScope)
}