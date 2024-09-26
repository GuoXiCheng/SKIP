package com.android.skip.ui.about

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.data.download.ApkDownloadViewModel
import com.android.skip.data.version.ApkVersionViewModel
import com.android.skip.dataclass.VersionState
import com.android.skip.ui.about.config.ConfigVersionButton
import com.android.skip.ui.about.download.ApkDownloadDialog
import com.android.skip.ui.about.version.ApkVersionButton
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import com.android.skip.util.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {
    private val configViewModel by viewModels<ConfigViewModel>()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    private val apkVersionViewModel by viewModels<ApkVersionViewModel>()

    private val apkDownloadViewModel by viewModels<ApkDownloadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.about, { finish() }, {
                    AboutGithub {
                        val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.about_github_subtitle)
                        }
                        startActivity(intent)
                    }

                    AboutDocs {
                        val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.about_docs_url)
                        }
                        startActivity(intent)
                    }
                    ApkVersionButton(apkVersionViewModel)
                    ConfigVersionButton(configViewModel)
                    ApkDownloadDialog(apkDownloadViewModel, apkVersionViewModel)
                }, {
                    DropdownMenuItem(
                        leadingIcon = { ResourceIcon(iconResource = R.drawable.help) },
                        text = { Text(stringResource(id = R.string.about_function_intro)) },
                        onClick = {
                            val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                                putExtra("url", R.string.about_function_intro_url)
                            }
                            startActivity(intent)
                        })
                })
            }
        }

        var latestVersion = ""
        apkVersionViewModel.versionPostState.observe(this) {
            if (it.status == VersionState.DISCOVER_LATEST) {
                val isNotUpdate =
                    DataStoreUtils.getSyncData(getString(R.string.store_not_update), false)
                if (!isNotUpdate) {
                    apkDownloadViewModel.changeDialogState(true)
                    latestVersion = it.latestVersion
                }
            }
        }

        apkDownloadViewModel.apkDownloadProcess.observe(this) {
            if (it == 100) {
                apkDownloadViewModel.changeDialogState(false)

                val filename = "SKIP-v$latestVersion.apk"
                val path = "${MyApp.context.filesDir}/apk"
                val apkFile = File(path, filename)
                val apkUri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    apkFile
                )

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(intent)
            }
        }

        configViewModel.configPostState.observe(this) {
            configViewModel.loadConfig(it)
        }
    }
}

@Composable
fun AboutGithub(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(
            title = R.string.about_github_title,
            subTitle = R.string.about_github_subtitle
        )
    }, onClick = onClick)
}

@Composable
fun AboutDocs(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(
            title = R.string.about_docs_title,
            subTitle = R.string.about_docs_subtitle
        )
    }, onClick = onClick)
}