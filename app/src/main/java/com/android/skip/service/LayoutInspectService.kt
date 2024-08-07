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
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.skip.NewMainActivity
import com.android.skip.R
import com.android.skip.SKIP_LAYOUT_INSPECT
import com.android.skip.manager.ToastManager
import com.android.skip.utils.DataStoreUtils
import com.blankj.utilcode.util.ScreenUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class LayoutInspectService: Service() {
    private var mMediaProjection: MediaProjection? = null
    private var mProjectionManager:MediaProjectionManager? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var isProcessingImage = false

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        // 开启前台服务
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("layout_inspect_service", "前台布局检查服务通知", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val it = Intent(this, NewMainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "layout_inspect_service")
            .setContentTitle("布局检查服务已准备就绪")
            .setContentText("布局检查服务将在运行一次后退出")
            .setSmallIcon(R.drawable.warning)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.warning))
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val keyCode = intent?.getIntExtra("keyCode", -1)

        if (mMediaProjection == null) {
            val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_CANCELED)
            val data = intent?.getParcelableExtra("data", Intent::class.java)
            if (resultCode == Activity.RESULT_OK && data != null) {
                mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                mMediaProjection = mProjectionManager?.getMediaProjection(resultCode, data)
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            isProcessingImage = true
            setupVirtualDisplay()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaProjection?.stop()
        DataStoreUtils.putSyncData(SKIP_LAYOUT_INSPECT, false)
    }

    @SuppressLint("WrongConstant")
    private fun setupVirtualDisplay() {
        val displayWidth = ScreenUtils.getScreenWidth()
        val displayHeight = ScreenUtils.getScreenHeight()
        val density = ScreenUtils.getScreenDensityDpi()

        val imageReader = ImageReader.newInstance(displayWidth, displayHeight, PixelFormat.RGBA_8888, 2)
        virtualDisplay = mMediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            displayWidth, displayHeight, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface, null, null
        )

        imageReader.setOnImageAvailableListener({reader ->
            if (!isProcessingImage) return@setOnImageAvailableListener

            val image = reader.acquireLatestImage()
            if (image != null) {
                val planes = image.planes
                val buffer = planes[0].buffer
                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride

                // Create Bitmap
                val bitmapWithStride = Bitmap.createBitmap(
                    rowStride / pixelStride,
                    displayHeight,
                    Bitmap.Config.ARGB_8888
                )
                bitmapWithStride.copyPixelsFromBuffer(buffer)
                val bitmap = Bitmap.createBitmap(bitmapWithStride, 0, 0, displayWidth, displayHeight)
                image.close()

                // 保存或处理bitmap
                val file = getOutputFile()
                val success = saveBitmapToFile(bitmap, file)
                if (success) {
                    ToastManager.showToast(this, "保存成功")
                } else {
                    ToastManager.showToast(this, "保存失败")
                }

                isProcessingImage = false
                stopSelf()
            }
        }, Handler(Looper.getMainLooper()))
    }

    private fun getOutputFile(): File {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(picturesDir, "screenshot_${System.currentTimeMillis()}.png")
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}