package com.android.skip.data.network.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MyApiService {
    @GET("/skip_config_v3.yaml")
    fun getSkipConfigV3(): Call<String>

    @GET
    suspend fun getConfigFromUrl(@Url url: String): Response<String>
}