package com.android.skip.data.version

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.data.network.MyApiNetwork
import com.android.skip.dataclass.VersionPostSchema
import com.android.skip.dataclass.VersionState
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils.getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApkVersionRepository @Inject constructor(
    private val myApiNetwork: MyApiNetwork
) {
    private val _versionPostState = MutableLiveData<VersionPostSchema>()
    val versionPostState: LiveData<VersionPostSchema> = _versionPostState

    private suspend fun changeVersionState(versionPostState: VersionPostSchema) =
        withContext(Dispatchers.IO) {
            _versionPostState.postValue(versionPostState)
            val version = myApiNetwork.fetchLatestVersion()
            if (version == AppUtils.getAppVersionName()) {
                _versionPostState.postValue(VersionPostSchema(VersionState.CURRENT_LATEST, version))
            } else {
                _versionPostState.postValue(
                    VersionPostSchema(
                        VersionState.DISCOVER_LATEST,
                        getString(R.string.about_discover_latest)
                    )
                )
            }
        }

    suspend fun checkVersion() {
        changeVersionState(
            VersionPostSchema(VersionState.PENDING, getString(R.string.checking))
        )
    }
}