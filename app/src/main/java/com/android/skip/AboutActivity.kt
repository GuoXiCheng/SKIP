package com.android.skip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.skip.compose.ConfirmDialog

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutActivityInterface()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutActivityInterface() {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(topBar = {
        SmallTopAppBar(title = {
            Text("关于")
        })
    }, content = { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            TextButton(onClick = { showDialog.value = true }) {
                Text(text = "开源地址: https://github.com/GuoXiCheng/SKIP")
            }
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
