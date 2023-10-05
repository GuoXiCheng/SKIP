package com.android.skip

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap

val mutableList = mutableListOf<AppData>()

class WhitelistActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mutableList.clear()

        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        packages.forEach {
            val appName = it.applicationInfo.loadLabel(packageManager).toString()
            val packageName = it.packageName

            if (!MyUtils.isExcludeApplication(appName, packageName, packageManager)) {
                mutableList.add(AppData(it.applicationInfo.loadIcon(packageManager), appName, false))
            }


        }

        setContent {
            Whitelist(mutableList)
        }

    }
}
data class AppData(
    val icon: Drawable,
    val name: String,
    var enabled: Boolean
)
@Composable
fun Whitelist (appList: List<AppData>) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {

            items(appList.size) { index ->
                val item = appList[index]
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(30.dp, 10.dp)) {

                    Image(
                        bitmap = item.icon.toBitmap().asImageBitmap(),
                        contentDescription = "${item.name} Icon", // 添加图标的描述，通常用于辅助功能
                        modifier = Modifier.size(48.dp) // 可选：设置图标的大小
                    )

                    Text(item.name, modifier = Modifier.weight(1f).padding(10.dp, 0.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    Switch(checked = item.enabled, onCheckedChange = {
                        item.enabled = it
                    })

                }
            }
        }
    }
}




