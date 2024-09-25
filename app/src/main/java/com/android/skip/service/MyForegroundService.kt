package com.android.skip.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.skip.R
import com.android.skip.ui.main.MainActivity

class MyForegroundService : Service() {
    private lateinit var notification: Notification

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "SKIP_FOREGROUND_SERVICE",
            "SKIP 无障碍服务",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        notification = NotificationCompat.Builder(this, "SKIP_FOREGROUND_SERVICE")
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_accessibility_service_running))
            .setSmallIcon(R.drawable.favicon32)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.favicon32))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground(2, notification)
        return START_STICKY
    }
}