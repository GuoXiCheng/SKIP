package com.android.skip.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.skip.R
import com.android.skip.ui.main.MainActivity

class MyForegroundService : Service() {
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
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "SKIP_FOREGROUND_SERVICE")
            .setContentTitle("SKIP 无障碍服务运行中")
            .setSmallIcon(R.drawable.info)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.info))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(2, notification)
    }
}