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
var accessibilityIsEnabled by mutableStateOf(false)


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
                        message = """
                            1.点击【已下载的应用】
                            2.点击【OneClick】
                            3.开启【使用OneClick】
                        """.trimIndent(),
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
//            if (accessibilityButtonClickState) {
//                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//                this.startActivity(intent)
//            }
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
//        CenterTextBox()
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
    /*
    OneClickTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Teal500) {

        }
        AccessibilityButton()
        Text(
            """version: ${BuildConfig.VERSION_NAME}""",
            Modifier.absoluteOffset(160.dp, 800.dp), fontSize = 13.sp,
            color = grey
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            TextButton(onClick = {
                expanded = !expanded
            }) {
                Text("机型选择", color = black, fontSize = 16.sp)
                val mobileName = when (selectedCurrentMobile) {
                    Mobile.XIAOMI -> Mobile.XIAOMI.name
                    Mobile.HUAWEI -> Mobile.HUAWEI.name
                    Mobile.OPPO -> Mobile.OPPO.name
                    Mobile.VIVO -> Mobile.VIVO.name
                    Mobile.MEIZU -> Mobile.MEIZU.name
                    Mobile.ONEPLUS -> Mobile.ONEPLUS.name
                }
                Text(
                    mobileName,
                    color = grey,
                    fontSize = 16.sp,
                    modifier = Modifier.width(100.dp),
                    textAlign = TextAlign.Center
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(Mobile.XIAOMI.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.XIAOMI
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text(Mobile.HUAWEI.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.HUAWEI
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text(Mobile.OPPO.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.OPPO
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text(Mobile.VIVO.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.VIVO
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text(Mobile.MEIZU.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.MEIZU
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text(Mobile.ONEPLUS.name) },
                    onClick = {
                        selectedCurrentMobile = Mobile.ONEPLUS
                        expanded = false
                    })
            }
        }
        when (selectedCurrentMobile) {
            Mobile.XIAOMI -> TurnOnBackgroundPermissionText(
                """
                1. 打开应用后，前往后台应用管理，长按应用，点击右侧锁定锁定后台。
                2. 长按应用图标，点击进入应用信息，打开自启动；点击省电策略，选择无限制。
            """.trimIndent()
            )
            Mobile.HUAWEI -> TurnOnBackgroundPermissionText(
                """
                1. 打开应用后，前往后台应用管理，下滑应用锁定后台。
                2. 打开手机管家，选择应用启动管理，找到OneClick，关闭自动管理。在手动管理中打开：允许自启动、允许管理启动、允许后台活动。
            """.trimIndent()
            )
            Mobile.OPPO -> TurnOnBackgroundPermissionText(
                """
                1. 打开应用后，前往后台应用管理，点击右上角的更多，点击锁定锁定后台。
                2. 长按应用图标，点击应用信息，打开允许自动启动。
                3. 打开系统设置，点击电池，点击自定义耗电保护，找到OneClick，设置为允许后台运行。返回上一页，找到应用速冻，关闭OneClick的自动速冻。
            """.trimIndent()
            )
            Mobile.VIVO -> TurnOnBackgroundPermissionText(
                """
                1. 打开应用后，前往后台应用管理，下滑应用锁定后台。
                2. 打开i管家，点击应用管理，点击权限管理，点击自启动，允许OneClick的自启动权限。
                3. 打开系统设置，点击电池，点击后台耗电管理，找到OneClick，并允许后台高耗电。
            """.trimIndent()
            )
            Mobile.MEIZU -> TurnOnBackgroundPermissionText(
                """
                1. 打开应用后，前往后台应用管理，长按应用，点击锁定锁定后台。
                2. 打开手机管理，点击隐私和权限，点击后台管理，设置OneClick为允许后台运行。
            """.trimIndent()
            )
            Mobile.ONEPLUS -> TurnOnBackgroundPermissionText(
                """
                 1. 打开应用后，前往后台应用管理，长按应用，点击锁定锁定后台。
                 2. 打开系统设置，点击电池，点击电量优化，找到OneClick并设置为不优化。
                """.trimIndent()
            )
        }

        Text("OneClick是一款免费开源的自动跳过APP开屏广告工具", modifier = Modifier.absoluteOffset(0.dp, 100.dp))
        Text(
            "由于无障碍服务会在应用进程结束后自动关闭，因此需要开启应用后台运行权限，具体设置方法如下：",
            modifier = Modifier.absoluteOffset(0.dp, 200.dp)
        )
    }
     */
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

//@Composable
//fun CenterTextBox() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.Center)
//    ) {
//        AnimatedVisibility(visible = expanded) {
//            Surface(
//                modifier = Modifier.size(140.dp, 120.dp),
//                color = lightGrey,
//                shape = shapes.small
//            ) {
//                Column {
//                    Text(
//                        "XIAOMI",
//                        color = lightGrey3,
//                        modifier = Modifier
//                            .size(140.dp, 40.dp)
//                            .padding(5.dp),
//                        fontSize = 18.sp,
//                        textAlign = TextAlign.Center
//                    )
//                    Divider(modifier = Modifier.height(1.dp), color = lightGrey2)
//                    Text(
//                        "HUAWEI",
//                        color = lightGrey3,
//                        modifier = Modifier
//                            .size(140.dp, 40.dp)
//                            .padding(5.dp),
//                        fontSize = 18.sp,
//                        textAlign = TextAlign.Center
//                    )
//                    Divider(modifier = Modifier.height(1.dp), color = lightGrey2)
//                    Text(
//                        "VIVO",
//                        color = lightGrey3,
//                        modifier = Modifier
//                            .size(140.dp, 40.dp)
//                            .padding(5.dp),
//                        fontSize = 18.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//        Text(
//            "机型选择",
//            color = Color.White,
//            fontSize = 12.sp,
//            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 100.dp)
//        )
//        TextButton(onClick = {
//            expanded = !expanded
//        }) {
//            Text("XIAOMI", color = Color.White)
//        }
//    }
//
//}
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
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
//        Button(onClick = {
//            accessibilityIsEnabled = !accessibilityIsEnabled
//        }, contentPadding = PaddingValues(0.dp), colors = ButtonDefaults.textButtonColors()) {
//            if (accessibilityIsEnabled) {
//                Image(
//                    modifier = Modifier.size(140.dp, 140.dp),
//                    painter = painterResource(id = R.drawable.disabled),
//                    contentDescription = "disabled",
//                )
//            } else {
//                Image(
//                    modifier = Modifier.size(140.dp, 140.dp),
//                    painter = painterResource(id = R.drawable.enabled),
//                    contentDescription = "enabled",
//                )
//            }
//        }
//    }
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
            modifier = Modifier.background(color = Color.White).padding(32.dp, 20.dp, 32.dp, 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("操作方式", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp))

            val operationList = when (selectedCurrentMobile) {
                Mobile.XIAOMI.name -> listOf(
                    "1.打开应用后，前往后台应用管理，长按应用，点击右侧锁定锁定后台。",
                    "2.长按应用图标，点击进入应用信息，打开自启动；点击省电策略，选择无限制。"
                )
                Mobile.HUAWEI.name -> listOf(
                    "1.打开应用后，前往后台应用管理，下滑应用锁定后台。",
                    "2.打开手机管家，选择应用启动管理，找到OneClick，关闭自动管理。在手动管理中打开：允许自启动、允许管理启动、允许后台活动。"
                )
                Mobile.OPPO.name -> listOf(
                    "1.打开应用后，前往后台应用管理，点击右上角的更多，点击锁定锁定后台。",
                    "2.长按应用图标，点击应用信息，打开允许自动启动。",
                    "3.打开系统设置，点击电池，点击自定义耗电保护，找到OneClick，设置为允许后台运行。返回上一页，找到应用速冻，关闭OneClick的自动速冻。"
                )
                Mobile.VIVO.name -> listOf(
                    "1.打开应用后，前往后台应用管理，下滑应用锁定后台。",
                    "2.打开i管家，点击应用管理，点击权限管理，点击自启动，允许OneClick的自启动权限。",
                    "3.打开系统设置，点击电池，点击后台耗电管理，找到OneClick，并允许后台高耗电。"
                )
                Mobile.MEIZU.name -> listOf(
                    "1.打开应用后，前往后台应用管理，长按应用，点击锁定锁定后台。",
                    "2.打开手机管理，点击隐私和权限，点击后台管理，设置OneClick为允许后台运行。"
                )
                Mobile.ONEPLUS.name -> listOf(
                    "1.打开应用后，前往后台应用管理，长按应用，点击锁定锁定后台。",
                    "2.打开系统设置，点击电池，点击电量优化，找到OneClick并设置为不优化。"
                )
                else -> listOf("")
            }
            for (item in operationList) {
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

//@Composable
//fun AccessibilityButton() {
//    Button(
//        onClick = {
//            accessibilityButtonClickState = true
//        },
//        contentPadding = ButtonDefaults.TextButtonContentPadding,
//        modifier = Modifier.absoluteOffset(120.dp, 700.dp)
//    ) {
//        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//        if (!accessibilityState) {
//            Icon(
//                Icons.Filled.PlayArrow,
//                contentDescription = "PlayArrow",
//                modifier = Modifier
//                    .size(ButtonDefaults.IconSize)
//                    .offset((-5).dp, 0.dp)
//            )
//            Text("启用无障碍服务")
//        } else {
//            Icon(
//                Icons.Filled.Close,
//                contentDescription = "Close",
//                modifier = Modifier
//                    .size(ButtonDefaults.IconSize)
//                    .offset((-5).dp, 0.dp)
//            )
//            Text("停用无障碍服务")
//        }
//
//    }
//
//}

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

//@Composable
//fun TurnOnBackgroundPermissionText(tx: String) {
//    Text(
//        tx, modifier = Modifier
//            .padding(10.dp)
//            .absoluteOffset(0.dp, 450.dp)
//    )
//}



