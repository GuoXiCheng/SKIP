package com.android.skip.ui.settings

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.data.SyncWorker
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.components.expandMenuItems
import com.android.skip.ui.components.notification.NotificationDialog
import com.android.skip.ui.components.notification.NotificationDialogViewModel
import com.android.skip.ui.settings.custom.CustomButton
import com.android.skip.ui.settings.recent.RecentButton
import com.android.skip.ui.settings.recent.RecentViewModel
import com.android.skip.ui.settings.strict.StrictButton
import com.android.skip.ui.settings.strict.StrictViewModel
import com.android.skip.ui.settings.theme.SwitchThemeButton
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.settings.tip.TipButton
import com.android.skip.ui.settings.tip.TipViewModel
import com.android.skip.ui.settings.update.AutoUpdateButton
import com.android.skip.ui.settings.update.AutoUpdateViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val tipViewModel by viewModels<TipViewModel>()

    private val strictViewModel by viewModels<StrictViewModel>()

    private val configViewModel by viewModels<ConfigViewModel>()

    private val notificationDialogViewModel by viewModels<NotificationDialogViewModel>()

    private val recentViewModel by viewModels<RecentViewModel>()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    private val autoUpdateViewModel by viewModels<AutoUpdateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.settings, { finish() }, {
                    AutoUpdateButton(autoUpdateViewModel)
                    RecentButton(recentViewModel)
                    TipButton(tipViewModel)
                    StrictButton(strictViewModel)
                    CustomButton(configViewModel) {
                        val intent = Intent(this, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.settings_custom_config_url)
                        }
                        startActivity(intent)
                    }
                    SwitchThemeButton(switchThemeViewModel)
                    NotificationDialog(notificationDialogViewModel) {
                        notificationDialogViewModel.changeDialogState(false)
                        tipViewModel.changeEnable(false)
                    }
                }, {
                    DropdownMenuItem(
                        leadingIcon = { ResourceIcon(iconResource = R.drawable.help) },
                        text = { Text(stringResource(id = R.string.settings_function_intro)) },
                        onClick = {
                            val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                                putExtra("url", R.string.settings_function_intro_url)
                            }
                            startActivity(intent)
                            expandMenuItems = false
                        })
                })
            }
        }

        tipViewModel.enable.observe(this) {
            if (it && !NotificationManagerCompat.from(MyApp.context).areNotificationsEnabled()) {
                notificationDialogViewModel.changeDialogState(true)
            }
        }

        recentViewModel.excludeFromRecent.observe(this) { exclude ->
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).let { manager ->
                manager.appTasks.forEach { task ->
                    task?.setExcludeFromRecents(exclude)
                }
            }
        }

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(12, TimeUnit.HOURS).build()
        val workManager = WorkManager.getInstance(this)

        autoUpdateViewModel.autoUpdate.observe(this) {
            when (it) {
                true -> workManager.enqueueUniquePeriodicWork(
                    getString(R.string.worker_sync),
                    ExistingPeriodicWorkPolicy.UPDATE,
                    periodicWorkRequest
                )

                false -> workManager.cancelUniqueWork(getString(R.string.worker_sync))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NotificationManagerCompat.from(MyApp.context).areNotificationsEnabled()) {
            tipViewModel.changeEnable(false)
        }
    }
}