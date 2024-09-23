package com.android.skip.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import com.android.skip.data.config.ConfigReadRepository
import com.android.skip.data.version.ApkVersionRepository
import com.blankj.utilcode.util.LogUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val configReadRepository: ConfigReadRepository,
    private val versionRepository: ApkVersionRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            LogUtils.d("SyncWorker doWork")
            val configPostSchema = configReadRepository.readConfig()
            configReadRepository.changeConfigPostState(configPostSchema)

            versionRepository.checkVersion()
            Result.success()
        } catch (e: Exception) {
            LogUtils.e(e)
            Result.failure()
        }
    }
}