package com.android.skip.data.network

import com.android.skip.data.network.api.MyApiService
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
}