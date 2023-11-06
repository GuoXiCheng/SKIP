package com.android.skip.manager

import android.content.Context
import com.android.skip.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileOutputStream

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

    fun getLatestVersion(): String {
        return try {
            val request = Request.Builder().url("$BASE_URL/latest_version.txt").build()
            client.newCall(request).execute().use { response ->
                return response.body()?.string()?.trim().toString()
            }
        } catch (e: Exception) {
            BuildConfig.VERSION_NAME.trim()
        }
    }

    fun downLoadNewAPK(latestVersion: String, context: Context) {
        try {
            val latestVersionAPK = "SKIP-v$latestVersion.apk"
            val request = Request.Builder().url("$BASE_URL/$latestVersionAPK").build()
            client.newCall(request).execute().use { response ->
                val fos = FileOutputStream(File(context.getExternalFilesDir(null), latestVersionAPK))
                fos.use {
                    fos.write(response.body()?.bytes())
                }
            }
        } catch (e: Exception) {
            LogManager.i(e.toString())
        }
    }
}