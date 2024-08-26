package com.android.skip.manager

import com.android.skip.dataclass.SkipConfigV3
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("/skip_config_v3.json")
    suspend fun getSkipConfigV3(): SkipConfigV3
}


object HttpManager2 {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:5173") // 替换为你的服务器地址
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}