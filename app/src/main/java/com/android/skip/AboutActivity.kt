package com.android.skip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.android.skip.compose.ConfirmDialog
import com.android.skip.compose.ScaffoldPage

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutActivityInterface{
                finish()
            }
        }
    }
}

@Composable
fun AboutActivityInterface(onClick: ()->Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    ScaffoldPage("关于", onClick = onClick, content = {
            TextButton(onClick = { showDialog.value = true }) {
                Text(text = "开源地址: https://github.com/GuoXiCheng/SKIP")
            }
    })

    if (showDialog.value) {
        ConfirmDialog(
            title = "启动应用",
            content = "SKIP想要打开Github，是否允许？" ,
            onDismiss = { showDialog.value = false },
            onAllow = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/GuoXiCheng/SKIP"))
                context.startActivity(intent)
                showDialog.value = false
            })
    }
}
