package com.android.skip.ui.alive

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.ui.alive.backstage.BackstageButton
import com.android.skip.ui.alive.backstage.BackstageViewModel
import com.android.skip.ui.alive.notificationbar.NotificationBarButton
import com.android.skip.ui.alive.notificationbar.NotificationBarViewModel
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.components.expandMenuItems
import com.android.skip.ui.components.notification.NotificationDialog
import com.android.skip.ui.components.notification.NotificationDialogViewModel
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import com.android.skip.util.MyToast
import com.blankj.utilcode.util.RomUtils
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class AliveActivity : AppCompatActivity() {
    private val backstageViewModel by viewModels<BackstageViewModel>()

    private val notificationBarViewModel by viewModels<NotificationBarViewModel>()

    private val notificationDialogViewModel by viewModels<NotificationDialogViewModel> ()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.alive, { finish() }, {
                    PowerSavingStrategyButton {
                        if (RomUtils.isXiaomi()) {
                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                            intent.data = Uri.parse("package:${packageName}")
                            startActivity(intent)
                        } else {
                            MyToast.show(R.string.alive_device_not_applicable)
                        }
                    }
                    SelfStartButton {
                        if (RomUtils.isXiaomi()) {
                            val intent = Intent()
                            intent.component = ComponentName(
                                "com.miui.securitycenter",
                                "com.miui.permcenter.autostart.AutoStartManagementActivity"
                            )
                            startActivity(intent)
                        } else {
                            MyToast.show(R.string.alive_device_not_applicable)
                        }
                    }
                    BackstageButton(backstageViewModel) {
                        backstageViewModel.changeDialogState(true)
                    }
                    NotificationBarButton(notificationBarViewModel)
                    WarningButton{
                        val searchContent = Build.MANUFACTURER + getString(R.string.alive_warn_search_content)
                        val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                            putExtra("address", "https://www.baidu.com/s?wd=${URLEncoder.encode(searchContent, "UTF-8")}")
                        }
                        startActivity(intent)
                    }
                    NotificationDialog(notificationDialogViewModel) {
                        notificationBarViewModel.changeEnable(false)
                        notificationDialogViewModel.changeDialogState(false)
                    }
                }, {
                    DropdownMenuItem(
                        leadingIcon = { ResourceIcon(iconResource = R.drawable.help) },
                        text = { Text(stringResource(id = R.string.alive_function_intro)) },
                        onClick = {
                            val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                                putExtra("url", R.string.alive_function_intro_url)
                            }
                            startActivity(intent)
                            expandMenuItems = false
                        })
                })
            }
        }

        notificationBarViewModel.enable.observe(this) {
            if (it && !NotificationManagerCompat.from(MyApp.context).areNotificationsEnabled()) {
                notificationDialogViewModel.changeDialogState(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NotificationManagerCompat.from(MyApp.context).areNotificationsEnabled()) {
            notificationBarViewModel.changeEnable(false)
        }
    }
}

@Composable
fun PowerSavingStrategyButton(onClick: () -> Unit = {}) {
    FlatButton(
        content = {
            RowContent(
                R.string.alive_power_saving,
                R.string.alive_power_saving_subtitle,
                { ResourceIcon(iconResource = R.drawable.counter_1) })
        }, onClick = onClick
    )
}

@Composable
fun SelfStartButton(onClick: () -> Unit = {}) {
    FlatButton(
        content = {
            RowContent(
                R.string.alive_self_start,
                R.string.alive_self_start_subtitle,
                { ResourceIcon(iconResource = R.drawable.counter_2) })
        }, onClick = onClick
    )
}

@Composable
fun WarningButton(onClick: () -> Unit={}){
    FlatButton(
        content = {
            RowContent(
                R.string.alive_warn,
                R.string.alive_warn_subtitle,
                { ResourceIcon(iconResource = R.drawable.warning) })
        }, onClick = onClick
    )
}