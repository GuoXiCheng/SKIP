package com.android.skip.data.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MyApiService {
    @GET
    suspend fun getConfigFromUrl(@Url url: String): Response<String>

    @GET("/latest_version.txt")
    suspend fun getLatestVersion(): Response<String>

    @GET("/SKIP-v{version}.apk")
    @Streaming
    suspend fun downloadAPK(@Path("version") latestVersion: String): Response<ResponseBody>
}