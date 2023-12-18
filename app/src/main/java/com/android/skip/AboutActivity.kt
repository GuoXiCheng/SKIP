package com.android.skip

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.ConfirmDialog
import com.android.skip.compose.FlatButton
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.manager.RectManager

class AboutActivity : BaseActivity() {
    @Composable
    override fun ProvideContent() {
        AboutActivityInterface {
            finish()
        }
    }
}

@Composable
fun AboutActivityInterface(onBackClick: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val openName = remember { mutableStateOf("") }
    val openUrl = remember { mutableStateOf("") }

    val githubName = stringResource(id = R.string.about_github_name)
    val githubAddress = stringResource(id = R.string.about_github_url)
    val skipDocsName = stringResource(id = R.string.about_skip_docs_name)
    val skipDocsAddress = stringResource(id = R.string.about_skip_docs_url)

    ScaffoldPage(stringResource(id = R.string.about), onBackClick = onBackClick, content = {
        FlatButton(
            content = {
                RowContent(githubName, githubAddress)
            }) {
            openName.value = githubName
            openUrl.value = githubAddress
            showDialog.value = true
        }

        FlatButton(
            content = {
                RowContent(skipDocsName, skipDocsAddress)
            }) {
            openName.value = skipDocsName
            openUrl.value = skipDocsAddress
            showDialog.value = true
        }

        FlatButton(content = {
            RowContent("当前版本：${BuildConfig.VERSION_NAME}")
        })

        FlatButton(content = {
            RowContent("当前分辨率：${RectManager.getMaxRect()}")
        })

    })

    OpenApplicationDialog(openName = openName.value, openUrl = openUrl.value, showDialog)
}

@Composable
fun OpenApplicationDialog(openName: String, openUrl: String, showDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    if (showDialog.value) {
        ConfirmDialog(
            title = "启动应用",
            content = "是否通过浏览器访问 $openName？",
            onDismiss = { showDialog.value = false },
            onAllow = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(openUrl))
                context.startActivity(intent)
                showDialog.value = false
            })
    }
}
