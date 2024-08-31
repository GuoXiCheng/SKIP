package com.android.skip.service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.skip.R
import com.android.skip.ui.main.MainActivity

class InspectService : Service() {
    private var mMediaProjection: MediaProjection? = null
    private var mProjectionManager: MediaProjectionManager? = null
    private var virtualDisplay: VirtualDisplay? = null

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
            .setContentTitle("SKIP 布局检查服务运行中")
            .setSmallIcon(R.drawable.info)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.info))
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mMediaProjection == null) {
            val resultCode = intent?.getIntExtra("resultCode", Activity.RESULT_CANCELED)
            val data = intent?.getParcelableExtra("data", Intent::class.java)
            if (resultCode == Activity.RESULT_OK && data != null) {
                mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                mMediaProjection = mProjectionManager?.getMediaProjection(resultCode, data)
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaProjection?.stop()
        virtualDisplay?.release()
    }
}