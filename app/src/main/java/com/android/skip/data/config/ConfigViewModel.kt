package com.android.skip.data.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configReadRepository: ConfigReadRepository,
    private val configLoadRepository: ConfigLoadRepository
) : ViewModel() {
    var configHashCode: LiveData<String> = configReadRepository.configHashCode

    private val observer = Observer<String> {
        val configMap = configReadRepository.handleConfig()
        configLoadRepository.loadConfig(configMap)
    }

    init {
        configReadRepository.configHashCode.observeForever(observer)
    }

    //    fun readConfig(context: Context) = configReadRepository.readConfig(context)
    fun readConfig() {
        viewModelScope.launch {
            configReadRepository.readConfig()
        }
    }

    override fun onCleared() {
        super.onCleared()
        configReadRepository.configHashCode.removeObserver(observer)
    }
}