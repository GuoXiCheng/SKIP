package com.android.skip.data.network.api

import retrofit2.Call
import retrofit2.http.GET

interface MyApiService {
    @GET("/skip_config_v3.yaml")
    fun getSkipConfigV3(): Call<String>
}