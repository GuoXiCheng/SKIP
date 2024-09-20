package com.android.skip.data.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skip.dataclass.ConfigPostSchema
import com.android.skip.dataclass.ConfigState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configReadRepository: ConfigReadRepository,
    private val configLoadRepository: ConfigLoadRepository
) : ViewModel() {
    var configPostState: LiveData<ConfigPostSchema> = configReadRepository.configPostState

    private val observer = Observer<ConfigPostSchema> {
        if (it.status == ConfigState.SUCCESS) {
            val configMap = configReadRepository.handleConfig(it)
            if (configMap != null) {
                configLoadRepository.loadConfig(configMap)
            }
        }
    }

    init {
        configReadRepository.configPostState.observeForever(observer)
    }

    fun readConfig() {
        viewModelScope.launch {
            val configPostSchema = configReadRepository.readConfig()
            configReadRepository.changeConfigPostState(configPostSchema)
        }
    }

    fun changeConfigPostState(configPostSchema: ConfigPostSchema) =
        configReadRepository.changeConfigPostState(configPostSchema)

    override fun onCleared() {
        super.onCleared()
        configReadRepository.configPostState.removeObserver(observer)
    }
}