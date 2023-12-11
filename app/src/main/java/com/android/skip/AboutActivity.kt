package com.android.skip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.android.skip.compose.ConfirmDialog
import com.android.skip.compose.CustomFloatingButton
import com.android.skip.compose.ScaffoldPage
import com.android.skip.ui.theme.AppTheme
import com.android.skip.viewmodel.ThemeViewModel

class AboutActivity : AppCompatActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                AboutActivityInterface {
                    finish()
                }
            }
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
        CustomFloatingButton(useElevation = false, containerColor = MaterialTheme.colorScheme.background, content = {
            Column {
                Text(githubName, fontSize = 16.sp)
                Text(githubAddress, fontSize = 12.sp)
            }
        }) {
            openName.value = githubName
            openUrl.value = githubAddress
            showDialog.value = true
        }

        CustomFloatingButton(useElevation = false, containerColor = MaterialTheme.colorScheme.background, content = {
            Column {
                Text(skipDocsName, fontSize = 16.sp)
                Text(skipDocsAddress, fontSize = 12.sp)
            }
        }) {
            openName.value = skipDocsName
            openUrl.value = skipDocsAddress
            showDialog.value = true
        }

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
