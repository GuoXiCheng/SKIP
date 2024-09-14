package com.android.skip.ui.record.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class InspectListViewModel @Inject constructor(
    private val inspectListRepository: InspectListRepository
) : ViewModel() {
    private val _delFileId = MutableLiveData<String>()
    val delFileId: LiveData<String> = _delFileId

    private var currentPagingSource: ZipFilePagingSource? = null

    fun deleteByFileId(fileId: String) = inspectListRepository.deleteByFileId(fileId)

    fun deleteAllFile() = inspectListRepository.deleteAllFile()

    fun changeDeleteFileId(fileId: String) {
        _delFileId.postValue(fileId)
    }

    fun reloadData() {
        currentPagingSource?.invalidate()
        filePagingData = createPager().flow.cachedIn(viewModelScope)
    }

    var filePagingData: Flow<PagingData<InspectRecordItem>> = createPager().flow.cachedIn(viewModelScope)

    private fun createPager(): Pager<Int, InspectRecordItem> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val newPagingSource = ZipFilePagingSource(inspectListRepository)
                currentPagingSource = newPagingSource
                newPagingSource
            }
        )
    }
}
