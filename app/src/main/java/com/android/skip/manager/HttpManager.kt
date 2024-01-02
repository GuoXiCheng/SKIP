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

    fun updateSkipConfigV2() {
        try {
            val request = Request.Builder().url("$BASE_URL/skip_config_v2.yaml").build()
            client.newCall(request).execute().use {response ->
                val bodyContent = response.body()?.string()
                val yaml = Yaml().load<Any>(bodyContent)
                SkipConfigManagerV2.setConfig(yaml)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun downloadNewAPK(latestVersion: String, context: Context, onDownloadProcess: (process: Int) -> Unit) {
        try {
            val latestVersionAPK = "SKIP-v$latestVersion.apk"
            val request = Request.Builder().url("$BASE_URL/$latestVersionAPK").build()

            client.newCall(request).execute().use { response ->
                val body = response.body()
                val contentLength = body?.contentLength() ?: 0
                body?.byteStream()?.apply {
                    val fos = FileOutputStream(File(context.getExternalFilesDir(null), latestVersionAPK))
                    val buffer = ByteArray(2048)
                    var len: Int
                    var downloaded = 0L

                    while (read(buffer).also { len = it } != -1) {
                        fos.write(buffer, 0, len)
                        downloaded += len
                        val progress = (downloaded.toFloat() / contentLength.toFloat()) * 100
                        onDownloadProcess(kotlin.math.floor(progress.toDouble()).toInt())
                    }
                    fos.flush()
                    fos.close()
                }
            }
        } catch (e: Exception) {
            LogManager.i(e.toString())
        }
    }
}