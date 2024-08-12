package com.android.skip.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.skip.NewMainActivity
import com.android.skip.R

class MyForegroundService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("skip_foreground_service", "SKIP 前台服务", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val it = Intent(this, NewMainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "skip_foreground_service")
            .setContentTitle("SKIP 前台服务")
            .setContentText("SKIP 前台服务运行中")
            .setSmallIcon(R.drawable.warning)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.warning))
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }
}