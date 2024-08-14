package com.android.skip

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.FlatButton
import com.android.skip.compose.OpenBrowserDialog
import com.android.skip.compose.PictureDialog
import com.android.skip.compose.ResourceIcon
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.enums.AccessibilityState
import com.android.skip.service.MyAccessibilityService
import com.android.skip.utils.AccessibilityUtils
import com.android.skip.utils.Constants
import com.android.skip.utils.DataStoreUtils
import com.android.skip.utils.NotificationUtils
import com.blankj.utilcode.util.ServiceUtils.startService
import java.net.URLEncoder

class KeepAliveActivity : BaseActivity() {

    @Composable
    override fun ProvideContent() {
        KeepAliveInterface() {
            finish()
        }
    }

    @Composable
    fun KeepAliveInterface(onBackClick: () -> Unit) {
        val context = LocalContext.current
        val showPicDialog = remember { mutableStateOf(false) }
        val showBrowserDialog = remember { mutableStateOf(false) }
        val checkForegroundAccessibility = remember {
            mutableStateOf(
                DataStoreUtils.getSyncData(Constants.SKIP_FOREGROUND_ACCESSIBILITY, false),
            )
        }
        ScaffoldPage(stringResource(id = R.string.alive), onBackClick = onBackClick, content = {
            PictureDialog(showPicDialog)
            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.alive_power_saving_title),
                    stringResource(id = R.string.alive_power_saving_subtitle),
                    { ResourceIcon(iconResource = R.drawable.counter_1) }
                )
            }) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            }
            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.alive_self_start_title),
                    stringResource(id = R.string.alive_self_start_subtitle),
                    { ResourceIcon(iconResource = R.drawable.counter_2) }
                )
            }) {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
                context.startActivity(intent)
            }
            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.alive_backstage_title),
                    stringResource(id = R.string.alive_backstage_subtitle),
                    { ResourceIcon(iconResource = R.drawable.counter_3) }
                )
            }) {
                showPicDialog.value = true
            }
            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.alive_warn_title),
                    stringResource(id = R.string.alive_warn_subtitle),
                    { ResourceIcon(iconResource = R.drawable.warning) }
                )
            }) {
                showBrowserDialog.value = true
            }
            FlatButton(content = {
                RowContent(
                    stringResource(id = R.string.alive_foreground_service_title),
                    stringResource(id = R.string.alive_foreground_service_subtitle),
                    { ResourceIcon(iconResource = R.drawable.counter_4) },
                    checkForegroundAccessibility.value,
                    {
                        checkForegroundAccessibility.value = it
                        DataStoreUtils.putSyncData(Constants.SKIP_FOREGROUND_ACCESSIBILITY, it)

                        if (it && !NotificationUtils.hasNotificationPermission()) {
                            NotificationUtils.startNotificationSettings(context)
                        }

                        val intent = Intent(Constants.FOREGROUND_ACCESSIBILITY_RECEIVER_ACTION)
                        intent.putExtra(Constants.FOREGROUND_ACCESSIBILITY_RECEIVER_ENABLED, it)
                        intent.setPackage(packageName)
                        sendBroadcast(intent)
                    }
                )
            })
        })

        val searchContent = Build.MANUFACTURER + stringResource(id = R.string.alive_warn_search_content)
        OpenBrowserDialog(
            openName = searchContent,
            openUrl = createBaiduSearchUrl(searchContent),
            showDialog = showBrowserDialog
        )
    }

    private fun createBaiduSearchUrl(query: String): String {
        return "https://www.baidu.com/s?wd=${URLEncoder.encode(query, "UTF-8")}"
    }
}

