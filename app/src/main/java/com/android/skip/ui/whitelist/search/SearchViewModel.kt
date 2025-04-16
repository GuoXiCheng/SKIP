package com.android.skip.ui.whitelist.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    fun changeQuery(query: String) {
        _query.postValue(query)
    }

    fun getQuery(): String {
        return _query.value ?: ""
    }
}