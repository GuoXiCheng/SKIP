package com.android.skip.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MyApiService {
    @GET
    suspend fun getConfigFromUrl(@Url url: String): Response<String>
}