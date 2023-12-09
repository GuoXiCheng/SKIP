package com.android.skip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.compose.ConfirmDialog
import com.android.skip.compose.CustomFloatingButton
import com.android.skip.compose.ScaffoldPage

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutActivityInterface {
                finish()
            }
        }
    }
}

@Composable
fun AboutActivityInterface(onClick: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val openName = remember { mutableStateOf("") }
    val openUrl = remember { mutableStateOf("") }

    val githubName = stringResource(id = R.string.about_github_name)
    val githubAddress = stringResource(id = R.string.about_github_url)
    val skipDocsName = stringResource(id = R.string.about_skip_docs_name)
    val skipDocsAddress = stringResource(id = R.string.about_skip_docs_url)

    ScaffoldPage(stringResource(id = R.string.about), onClick = onClick, content = {
        CustomFloatingButton(useElevation = false, containerColor = Color.White, content = {
            Column {
                Text(githubName, fontSize = 16.sp)
                Text(githubAddress, fontSize = 12.sp)
            }
        }) {
            openName.value = githubName
            openUrl.value = githubAddress
            showDialog.value = true
        }

        CustomFloatingButton(useElevation = false, containerColor = Color.White, content = {
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
            content = "SKIP想要打开$openName，是否允许？",
            onDismiss = { showDialog.value = false },
            onAllow = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(openUrl))
                context.startActivity(intent)
                showDialog.value = false
            })
    }
}
