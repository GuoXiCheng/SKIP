package com.android.skip

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.android.skip.compose.AboutButton
import com.android.skip.compose.ConfirmDialog
import com.android.skip.compose.DownloadProcessDialog
import com.android.skip.compose.KeepAliveButton
import com.android.skip.compose.LayoutInspectButton
import com.android.skip.compose.SettingsButton
import com.android.skip.compose.StartButton
import com.android.skip.compose.WhitelistButton
import com.android.skip.dataclass.PackageInfoV2
import com.android.skip.manager.HttpManager
import com.android.skip.manager.SkipConfigManagerV2
import com.android.skip.manager.WhitelistManager
import com.android.skip.utils.AccessibilityUtils
import com.android.skip.utils.DataStoreUtils
import com.android.skip.viewmodel.StartButtonViewModel
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.concurrent.thread

var showUpdateDialog by mutableStateOf(false)

var showDownloadDialog by mutableStateOf(false)

var showApkInstallDialog by mutableStateOf(false)

var apkDownloadProgress by mutableStateOf(0f)

var apkLatestVersionText by mutableStateOf(BuildConfig.VERSION_NAME.trim())

class NewMainActivity : BaseActivity() {
    private val startButtonViewModel: StartButtonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val yaml = Yaml().load<List<PackageInfoV2>>(assets.open("skip_config_v2.yaml"))
            SkipConfigManagerV2.setConfig(yaml)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Composable
    override fun ProvideContent() {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 64.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            StartButton(startButtonViewModel)
            WhitelistButton()
            KeepAliveButton()
            LayoutInspectButton()
            SettingsButton()
            AboutButton()

            if (showUpdateDialog) {
                ConfirmDialog(
                    title = "发现新版本",
                    content = "是否立即下载更新？",
                    onDismiss = { showUpdateDialog = false },
                    onAllow = {
                        showUpdateDialog = false
                        showDownloadDialog = true
                    })
            }

            if (showDownloadDialog) {
                DownloadProcessDialog()

                thread {
                    HttpManager.downloadNewAPK(apkLatestVersionText, context) { it ->
                        apkDownloadProgress = it * 0.01f
                        if (it == 100) {
                            showDownloadDialog = false
                            showApkInstallDialog = true
                        }
                    }
                }
            }

            if (showApkInstallDialog) {
                ConfirmDialog(
                    title = "下载完成",
                    content = "是否立即安装新版本？",
                    onDismiss = { showApkInstallDialog = false },
                    onAllow = {
                        showApkInstallDialog = false

                        val filename = "SKIP-v$apkLatestVersionText.apk"
                        val apkFile = File(context.getExternalFilesDir(null), filename)
                        val apkUri = FileProvider.getUriForFile(
                            context,
                            context.applicationContext.packageName + ".provider",
                            apkFile
                        )

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        context.startActivity(intent)
                    })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startButtonViewModel.changeButtonState(AccessibilityUtils.getAccessibilityState(this))

        WhitelistManager.setWhitelist(lifecycleScope, applicationContext)

        if (DataStoreUtils.getSyncData(SKIP_AUTO_SYNC_CONFIG, false)) {
            thread {
                HttpManager.updateSkipConfigV2()
            }
        }

        if (DataStoreUtils.getSyncData(SKIP_AUTO_CHECK_UPDATE, false)) {
            thread {
                val latestVersion = HttpManager.getLatestVersion()
                if (latestVersion != BuildConfig.VERSION_NAME.trim()) {
                    apkLatestVersionText = latestVersion
                    showUpdateDialog = true
                }
            }
        }
    }
}

