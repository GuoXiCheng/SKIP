package com.android.skip.service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.android.skip.R
import com.android.skip.ui.inspect.start.StartInspectRepository
import com.android.skip.ui.main.MainActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class InspectService : Service() {
    private var mMediaProjection: MediaProjection? = null
    private var mProjectionManager: MediaProjectionManager? = null
    private var virtualDisplay: VirtualDisplay? = null

    @Inject
    lateinit var startInspectRepository: StartInspectRepository

    @Inject
    lateinit var accessibilityInspectRepository: AccessibilityInspectRepository

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            "INSPECT_SERVICE",
            "布局检查服务",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "INSPECT_SERVICE")
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_inspect_service_running))
            .setSmallIcon(R.drawable.favicon32)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.favicon32))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mMediaProjection == null) {
            val resultCode = intent?.getIntExtra("result_code", Activity.RESULT_CANCELED)
            val resultData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra("result_data", Intent::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent?.getParcelableExtra("result_data")
            }
            if (resultCode == Activity.RESULT_OK && resultData != null) {
                mProjectionManager =
                    getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                mMediaProjection = mProjectionManager?.getMediaProjection(resultCode, resultData)
                setupVirtualDisplay()
            }
        }

        startInspectRepository.changeInspectState(true)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mProjectionManager = null
        mMediaProjection?.stop()
        mMediaProjection = null
        virtualDisplay?.release()
        virtualDisplay = null

        startInspectRepository.changeInspectState(false)
    }

    private fun setupVirtualDisplay() {
        // 在调用 createVirtualDisplay 之前，先注册回调
        mMediaProjection?.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                // 当 MediaProjection 停止时，执行清理操作
                virtualDisplay?.release()
                virtualDisplay?.release()
            }
        }, null)

        val displayWidth = ScreenUtils.getScreenWidth()
        val displayHeight = ScreenUtils.getScreenHeight()
        val density = ScreenUtils.getScreenDensityDpi()

        val imageReader =
            ImageReader.newInstance(displayWidth, displayHeight, PixelFormat.RGBA_8888, 2)
        virtualDisplay = mMediaProjection?.createVirtualDisplay(
            "SCREEN_CAPTURE",
            displayWidth, displayHeight, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface, null, null
        )

        imageReader.setOnImageAvailableListener({ reader ->
            if (!accessibilityInspectRepository.isStartCaptureScreen) {
                // 清理缓冲区
                reader.acquireLatestImage()?.close()
                return@setOnImageAvailableListener
            }

            val image = reader.acquireLatestImage()
            if (image != null) {
                val planes = image.planes
                val buffer = planes[0].buffer
                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * displayWidth

                // 计算 Bitmap 的宽度和高度
                val bitmapWithStride = Bitmap.createBitmap(
                    displayWidth + rowPadding / pixelStride, displayHeight, Bitmap.Config.ARGB_8888
                )
                bitmapWithStride.copyPixelsFromBuffer(buffer)
                val bitmap =
                    Bitmap.createBitmap(bitmapWithStride, 0, 0, displayWidth, displayHeight)
                image.close()

                // save bitmap
                val file = File(
                    accessibilityInspectRepository.filepath,
                    "${accessibilityInspectRepository.fileId}.jpeg"
                )

                try {
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                    outputStream.flush()
                    outputStream.close()
                } catch (e: IOException) {
                    LogUtils.e(e)
                }

                accessibilityInspectRepository.stopCaptureScreen()
            }
        }, Handler(Looper.getMainLooper()))
    }
}