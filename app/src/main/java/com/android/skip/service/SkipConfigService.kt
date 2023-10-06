package com.android.skip.service

import com.android.skip.dataclass.PackageInfo
import retrofit2.Call
import retrofit2.http.GET

interface SkipConfigService {

    @GET("skip_config_v1.json")
    fun getPackageInfo(): Call<List<PackageInfo>>
}