package com.android.skip.manager

import okhttp3.OkHttpClient
import okhttp3.Request
import org.yaml.snakeyaml.Yaml

object HttpManager {
    private const val BASE_URL = "https://guoxicheng.github.io/SKIP"
    private val client = OkHttpClient()

    fun updateSkipConfig(): Boolean {
        return try {
            val request = Request.Builder().url("$BASE_URL/skip_config.yaml").build()
            client.newCall(request).execute().use { response ->
                val bodyContent = response.body()?.string()
                val yaml = Yaml().load<Any>(bodyContent)
                SkipConfigManager.setConfig(yaml)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}