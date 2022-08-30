package com.android.oneclick

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.oneclick.ui.theme.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

var accessibilityState by mutableStateOf(false)
var accessibilityButtonClickState by mutableStateOf(false)
var alertDialogPositiveButtonClickState by mutableStateOf(false)
var expanded by mutableStateOf(false)
var selectedCurrentMobile by mutableStateOf(Mobile.XIAOMI.name)


class MainActivity : ComponentActivity() {
    private var accessibilityEnabled = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.white, null)

        setContent {
            MainSurface()

            if (accessibilityButtonClickState) {
                if (!accessibilityState) {
                    AlertDialog(
                        context = this,
                        title = "启用无障碍服务",
                        message = "已下载的应用>SKIP>使用SKIP".trimIndent(),
                        negativeText = "再想想",
                        positiveText = "去开启"
                    )
                } else {
                    AlertDialog(
                        context = this,
                        title = "停用无障碍服务",
                        message = null,
                        negativeText = "再想想",
                        positiveText = "去停用"
                    )
                }
                accessibilityButtonClickState = false
            }

            if (alertDialogPositiveButtonClickState) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                this.startActivity(intent)
                alertDialogPositiveButtonClickState = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accessibilityEnabled = Settings.Secure.getInt(
            this.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED
        )
        accessibilityState = accessibilityEnabled == 1
    }
}

@Composable
@Preview
fun MainSurface() {
    OneClickTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = green) {

        }
        TopBox()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "机型选择",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.offset(0.dp, 200.dp)
            )
            TextButton(onClick = {
                expanded = !expanded
            }, modifier = Modifier.offset(0.dp, 200.dp)) {
                Text(selectedCurrentMobile, color = Color.White, fontSize = 18.sp)
            }
        }
        BottomBox()
        CenterButtonBox()

    }
}

@Composable
fun TopBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Column(modifier = Modifier.padding(0.dp, 60.dp)) {
            Text("SKIP", color = Color.White, fontSize = 36.sp)
            Row {
                Text("是一款免费开源的自动跳过APP开屏广告的工具", color = Color.White, fontSize = 16.sp)
            }

        }

    }

}


@Composable
fun CenterMenu(mobileList: List<String>) {
    Text("", modifier = Modifier.size(140.dp, (mobileList.size * 40).dp))
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        Surface(
            modifier = Modifier.size(140.dp, (mobileList.size * 40).dp),
            color = lightGrey,
            shape = shapes.small
        ) {
            Column {
                for (item in mobileList) {
                    Text(
                        item,
                        color = lightGrey3,
                        modifier = Modifier
                            .size(140.dp, 40.dp)
                            .border(0.5.dp, lightGrey2)
                            .padding(5.dp)
                            .clickable {
                                selectedCurrentMobile = item
                                expanded = false
                            },
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun CenterButtonBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {

        Button(onClick = {
            accessibilityButtonClickState = true
        }, contentPadding = PaddingValues(0.dp), colors = ButtonDefaults.textButtonColors()) {
            if (accessibilityState) {
                Image(
                    modifier = Modifier.size(140.dp, 140.dp),
                    painter = painterResource(id = R.drawable.disabled),
                    contentDescription = "disabled",
                )
            } else {
                Image(
                    modifier = Modifier.size(140.dp, 140.dp),
                    painter = painterResource(id = R.drawable.enabled),
                    contentDescription = "enabled",
                )
            }
        }
        CenterMenu(
            listOf(
                Mobile.XIAOMI.name,
                Mobile.HUAWEI.name,
                Mobile.MEIZU.name,
                Mobile.VIVO.name,
                Mobile.OPPO.name,
                Mobile.ONEPLUS.name
            )
        )
    }
}

@Composable
fun BottomBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomStart)
    ) {

        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(32.dp, 20.dp, 32.dp, 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "操作方式",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
            )
            val rs = LocalContext.current.resources
            val operationString = when (selectedCurrentMobile) {
                Mobile.XIAOMI.name -> rs.getString(R.string.XIAOMI)
                Mobile.HUAWEI.name -> rs.getString(R.string.HUAWEI)
                Mobile.OPPO.name -> rs.getString(R.string.OPPO)
                Mobile.VIVO.name -> rs.getString(R.string.VIVO)
                Mobile.MEIZU.name -> rs.getString(R.string.MEIZU)
                Mobile.ONEPLUS.name -> rs.getString(R.string.ONEPLUS)
                else -> ""
            }
            for (item in operationString.split("#")) {
                Text(
                    item,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
                )
            }
            Text(
                "注意事项",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
            )
            Text(
                "由于无障碍服务会在应用进程结束后自动关闭，因此需要开启应用后台运行权限",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
            )
        }
    }
}


@Composable
fun AlertDialog(
    context: Context, title: CharSequence,
    message: CharSequence?, negativeText: CharSequence,
    positiveText: CharSequence
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(negativeText, null)
        .setPositiveButton(positiveText) { _, _ ->
            alertDialogPositiveButtonClickState = true
        }
        .show()
}



