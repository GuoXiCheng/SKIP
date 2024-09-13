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
import androidx.compose.runtime.Composable
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
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class AliveActivity : AppCompatActivity() {
    private val backstageViewModel by viewModels<BackstageViewModel>()

    private val notificationBarViewModel by viewModels<NotificationBarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(R.string.alive, { finish() }, {
                    PowerSavingStrategyButton {
                        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        intent.data = Uri.parse("package:${packageName}")
                        startActivity(intent)
                    }
                    SelfStartButton {
                        val intent = Intent()
                        intent.component = ComponentName(
                            "com.miui.securitycenter",
                            "com.miui.permcenter.autostart.AutoStartManagementActivity"
                        )
                        startActivity(intent)
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
                })
            }
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