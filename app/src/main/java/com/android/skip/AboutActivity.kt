package com.android.skip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.FlatButton
import com.android.skip.compose.OpenBrowserDialog
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
            RowContent(stringResource(id = R.string.about_current_version) + BuildConfig.VERSION_NAME)
        })

        FlatButton(content = {
            RowContent(stringResource(id = R.string.about_current_resolution) + RectManager.getMaxRect())
        })

    })

    OpenBrowserDialog(openName = openName.value, openUrl = openUrl.value, showDialog)
}
