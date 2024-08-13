package com.android.skip.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.android.skip.SKIPApp

object NotificationUtils {
    fun startNotificationSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, SKIPApp.myPackageName)
            }
            context.startActivity(intent)
        }
    }

    fun hasNotificationPermission(): Boolean {
        return NotificationManagerCompat.from(SKIPApp.context).areNotificationsEnabled()
    }
}