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
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.data.config.ConfigViewModel
import com.android.skip.data.version.ApkVersionViewModel
import com.android.skip.dataclass.VersionState
import com.android.skip.ui.about.config.ConfigVersionButton
import com.android.skip.ui.about.download.ApkDownloadDialog
import com.android.skip.data.download.ApkDownloadViewModel
import com.android.skip.ui.about.version.ApkVersionButton
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint

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

        apkVersionViewModel.versionPostState.observe(this) {
            if (it.status == VersionState.DISCOVER_LATEST) {
                apkDownloadViewModel.changeDialogState(true)
            }
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