package com.android.skip.data.download

import com.android.skip.data.network.MyApiNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApkDownloadRepository @Inject constructor(
    private val myApiNetwork: MyApiNetwork
) {
    suspend fun downloadAPK(latestVersion: String, onDownloadProcess: (process: Int) -> Unit) =
        withContext(Dispatchers.IO) {
            myApiNetwork.fetchAPK(latestVersion, onDownloadProcess)
        }
}