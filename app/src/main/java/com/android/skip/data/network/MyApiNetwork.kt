package com.android.skip.data.network

import com.android.skip.data.network.api.MyApiService
import com.blankj.utilcode.util.AppUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyApiNetwork @Inject constructor() {
    private val myApiService = ServiceCreator.create(MyApiService::class.java)

    suspend fun fetchConfigFromUrl(url: String): String {
        val response = myApiService.getConfigFromUrl(url)
        if (response.isSuccessful) {
            return response.body() ?: throw RuntimeException("Response body is null")
        } else {
            throw RuntimeException("Failed with error code: ${response.code()}")
        }
    }

    suspend fun fetchLatestVersion(): String {
        val response = myApiService.getLatestVersion()
        return if (response.isSuccessful) {
            response.body().toString()
        } else {
            AppUtils.getAppVersionName()
        }
    }
}