package com.android.skip.data.network

import com.android.skip.MyApp
import com.android.skip.data.network.api.MyApiService
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.floor

@Singleton
class MyApiNetwork @Inject constructor() {
    private val myApiService = ServiceCreator.create(MyApiService::class.java)

    suspend fun fetchConfigFromUrl(url: String): String {
        val response = myApiService.getConfigFromUrl(url)
        if (response.isSuccessful) {
            return response.body() ?: throw RuntimeException("Response body is null")
        } else {
            throw RuntimeException("Failed with error code: ${response.code()}")
        }
    }

    suspend fun fetchLatestVersion(): String {
        val response = myApiService.getLatestVersion()
        return if (response.isSuccessful) {
            response.body().toString()
        } else {
            AppUtils.getAppVersionName()
        }
    }

    suspend fun fetchAPK(latestVersion: String, onDownloadProcess: (process: Int)->Unit) {
        try {
            val response = myApiService.downloadAPK(latestVersion)

            if (response.isSuccessful) {
                response.body()?.let { body->
                    val contentLength = body.contentLength()
                    val inputStream = body.byteStream()

                    val path = "${MyApp.context.filesDir}/apk"
                    val fileDir = File(path)
                    if (!fileDir.exists()) {
                        fileDir.mkdir()
                    } else {
                        FileUtils.deleteFilesInDir(fileDir)
                    }

                    val file = File(path, "SKIP-v$latestVersion.apk")
                    val outputStream = FileOutputStream(file)

                    val buffer = ByteArray(2048)
                    var downloaded = 0L
                    var read: Int

                    withContext(Dispatchers.IO) {
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                            downloaded += read
                            val progress = (downloaded.toFloat() / contentLength.toFloat()) * 100
                            withContext(Dispatchers.Main) {
                                onDownloadProcess(floor(progress).toInt())
                            }
                        }
                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                    }
                }
            } else {
                throw Exception("Failed to download file: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }
}