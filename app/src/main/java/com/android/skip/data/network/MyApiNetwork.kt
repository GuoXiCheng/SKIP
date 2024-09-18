package com.android.skip.data.network

import com.android.skip.data.network.api.MyApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class MyApiNetwork @Inject constructor() {
    private val myApiService = ServiceCreator.create(MyApiService::class.java)

    suspend fun fetchSkipConfigV3() = myApiService.getSkipConfigV3().await()

    suspend fun fetchConfigFromUrl(url: String): String {
        val response = myApiService.getConfigFromUrl(url)
        if (response.isSuccessful) {
            return response.body() ?: throw RuntimeException("Response body is null")
        } else {
            throw RuntimeException("Failed with error code: ${response.code()}")
        }
    }

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation->
            enqueue(object: Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}