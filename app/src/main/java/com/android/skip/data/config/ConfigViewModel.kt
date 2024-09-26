package com.android.skip.data.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skip.R
import com.android.skip.dataclass.ConfigPostSchema
import com.android.skip.dataclass.ConfigState
import com.blankj.utilcode.util.StringUtils.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configReadRepository: ConfigReadRepository,
    private val configLoadRepository: ConfigLoadRepository
) : ViewModel() {
    var configPostState: LiveData<ConfigPostSchema> = configReadRepository.configPostState

    fun readConfig() {
        viewModelScope.launch {
            val configPostSchema = configReadRepository.readConfig()
            configReadRepository.changeConfigPostState(configPostSchema)
        }
    }

    fun loadConfig(configPostSchema: ConfigPostSchema) {
        if (configPostSchema.status == ConfigState.SUCCESS) {
            val configMap = configReadRepository.handleConfig(configPostSchema)
            if (configMap != null) {
                configLoadRepository.loadConfig(configMap)
            } else {
                changeConfigPostState(ConfigPostSchema(ConfigState.FAIL, getString(R.string.invalid_config)))
            }
        }
    }

    fun changeConfigPostState(configPostSchema: ConfigPostSchema) =
        configReadRepository.changeConfigPostState(configPostSchema)
}