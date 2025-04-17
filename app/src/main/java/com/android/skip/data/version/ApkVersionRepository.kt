package com.android.skip.data.version

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.data.network.MyApiNetwork
import com.android.skip.dataclass.SemanticVersion
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
            val version2 = AppUtils.getAppVersionName()
            try {
                _versionPostState.postValue(versionPostState)
                val version1 = myApiNetwork.fetchLatestVersion()
                if (isVersionGreater(version1, version2)) {
                    _versionPostState.postValue(
                        VersionPostSchema(
                            VersionState.DISCOVER_LATEST,
                            getString(R.string.about_discover_latest),
                            version1
                        )
                    )
                } else {
                    _versionPostState.postValue(
                        VersionPostSchema(
                            VersionState.CURRENT_LATEST,
                            version2,
                            version2
                        )
                    )
                }
            } catch (e: Exception) {
                _versionPostState.postValue(
                    VersionPostSchema(
                        VersionState.CURRENT_LATEST,
                        version2,
                        version2
                    )
                )
            }
        }

    suspend fun checkVersion() {
        changeVersionState(
            VersionPostSchema(VersionState.PENDING, getString(R.string.checking), String())
        )
    }

    private fun parseVersion(version: String): SemanticVersion {
        val parts = version.split(".")
        val major = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
        val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
        return SemanticVersion(major, minor, patch)
    }

    private fun isVersionGreater(version1: String, version2: String): Boolean {
        val v1 = parseVersion(version1)
        val v2 = parseVersion(version2)
        return when {
            v1.major > v2.major -> true
            v1.major < v2.major -> false
            v1.minor > v2.minor -> true
            v1.minor < v2.minor -> false
            v1.patch > v2.patch -> true
            else -> false
        }
    }
}