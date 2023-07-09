package com.android.skip

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.ui.theme.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.*

var accessibilityState by mutableStateOf(false)

var alertDialogPositiveButtonClickState by mutableStateOf(false)
var expanded by mutableStateOf(false)
var selectedCurrentMobile by mutableStateOf(Mobile.XIAOMI.name)

// 无障碍服务启用停用按钮
var isAccessibilityBtnClicked by mutableStateOf(false)
// 后台任务管理
var isBackendTaskBtnClicked by mutableStateOf(false)
// 自启动管理
var isAutoStartBtnClicked by mutableStateOf(false)
// 省电策略按钮
var isPowerSavingBtnClicked by mutableStateOf(false)


class MainActivity : ComponentActivity() {
    private fun openAppInfo() {
        val packageName = packageName
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }

    private fun openAutoStartSettings(context: Context) {
        try {
            val intent = Intent()

            val manufacturer = Build.MANUFACTURER.lowercase(Locale.ENGLISH)
            when {
                manufacturer.contains("xiaomi") -> {
                    intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                }
                manufacturer.contains("oppo") -> {
                    intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                }
                manufacturer.contains("vivo") -> {
                    intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                }
                manufacturer.contains("oneplus") -> {
                    intent.component = ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity")
                }
                manufacturer.contains("huawei") -> {
                    intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
                }
                else -> {
                    openAppInfo()
                }
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            openAppInfo()
        }
    }

    private fun openBatteryOptimizationSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        } catch (e: Exception) {
            openAppInfo()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.white, null)

        setContent {
            MainSurface()

            if (isAccessibilityBtnClicked) {
                if (!accessibilityState) {
                    AlertDialog(
                        context = this,
                        title = "启用无障碍服务",
                        message = "已下载的应用 > SKIP > 打开「使用SKIP」",
                        negativeText = "再想想",
                        positiveText = "去开启"
                    )
                } else {
                    AlertDialog(
                        context = this,
                        title = "停用无障碍服务",
                        message = "已下载的应用 > SKIP > 关闭「使用SKIP」",
                        negativeText = "再想想",
                        positiveText = "去停用"
                    )
                }
                isAccessibilityBtnClicked = false
            }


            when {
                alertDialogPositiveButtonClickState -> {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    this.startActivity(intent)
                    alertDialogPositiveButtonClickState = false
                }
                isAutoStartBtnClicked -> {
                    openAutoStartSettings(this)
                    isAutoStartBtnClicked = false
                }
                isPowerSavingBtnClicked -> {
                    openBatteryOptimizationSettings(this)
                    isPowerSavingBtnClicked = false
                }
                isBackendTaskBtnClicked -> {
                    ImageDialog()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        accessibilityState = MyUtils.isAccessibilitySettingsOn(this)
    }
}


@Composable
fun MainSurface() {
    OneClickTheme {

        val res = LocalContext.current.resources

        PageHeader(res.getString(R.string.app_name), "是一款免费开源的自动跳过APP开屏广告的工具")

//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Box(modifier = Modifier.offset(y = (-150).dp)) {
//                ModelSelectionBtn()
//            }
//        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box (modifier = Modifier.offset(y=(-50).dp)) {
                AccessibilityControlBtn()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box (modifier = Modifier.offset(y=(100).dp)) {
                AccessibilityTextBox()
            }
        }

//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            val configuration = LocalConfiguration.current
//            val screenWidthDp = configuration.screenWidthDp
//            val screenHeightDp = configuration.screenHeightDp
//
//            val xOffset = screenWidthDp / 2
//            val yOffset = screenHeightDp / 2.5
//
//            Box(modifier = Modifier.offset(x = (xOffset).dp - 75.dp, y = (yOffset).dp)) {
//                ModelSelectionMenu(
//                    listOf(
//                        Mobile.XIAOMI.name,
//                        Mobile.HUAWEI.name,
//                        Mobile.MEIZU.name,
//                        Mobile.VIVO.name,
//                        Mobile.OPPO.name,
//                        Mobile.ONEPLUS.name
//                    )
//                )
//            }
//
//        }


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            TipBox()
            PageFooter()
        }

    }
}

@Composable
fun PageHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 20.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = subtitle,
            color = Color.White,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun AccessibilityTextBox() {
    Row  {
        if(accessibilityState) {
            Text(text = "无障碍服务功能: 启用中", color = Color.White)
        } else {
            Text(text = "无障碍服务功能: 未启用", color = Color.White)
        }

    }
}

@Composable
fun TipBox() {

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .height(270.dp)
            .fillMaxWidth()
            .padding(30.dp, 20.dp)
    ) {
        Text(
            "操作方式",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
        )

        Row {
            TipText("第一步：进入「")
            ClickableText("后台任务管理", onClick = { clicked -> isBackendTaskBtnClicked = clicked })
            TipText("」,长按「SKIP」,锁定")
        }

        Row {
            TipText("第二步：进入「")
            ClickableText("省电策略", onClick = { clicked -> isPowerSavingBtnClicked = clicked })
            TipText("」,无限制")
        }

        Row {
            TipText("第三步：进入「")
            ClickableText("自启动管理", onClick = { clicked -> isAutoStartBtnClicked = clicked })
            TipText("」,找到「SKIP」,允许")
        }

        Text(
            "注意事项",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
        )

        TipText("由于无障碍服务会在应用进程结束后自动关闭，因此需要完成上述操作开启应用后台运行权限")


    }
}

@Composable
fun TipText(text: String) {
    Text(
        text,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
    )
}


@Composable
fun ClickableText(text: String, onClick: (Boolean) -> Unit) {
    val annotatedString = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline),
            start = 0,
            end = length
        )
        addStringAnnotation(
            tag = "Clickable",
            annotation = "Details",
            start = 0,
            end = length
        )
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations("Clickable", offset, offset)
                .firstOrNull()?.let { _ ->
                    onClick(true)
                }
        }
    )
}


@Composable
fun ModelSelectionBtn() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "机型选择",
            color = Color.White,
            fontSize = 12.sp
        )
        TextButton(
            onClick = { expanded = !expanded }
        ) {
            Text(selectedCurrentMobile, color = Color.White, fontSize = 18.sp)
        }
    }
}


@Composable
fun ModelSelectionMenu(mobileList: List<String>) {
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        Surface(
            modifier = Modifier.size(150.dp, (mobileList.size * 40).dp),
            color = lightGrey,
            shape = shapes.small
        ) {
            Column {
                for (item in mobileList) {
                    Text(
                        item,
                        color = lightGrey3,
                        modifier = Modifier
                            .size(150.dp, 40.dp)
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
fun AccessibilityControlBtn() {
    Button(
        onClick = {
            isAccessibilityBtnClicked = true
        },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.textButtonColors()
    ) {
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
}

@Composable
fun PromptBox() {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .height(270.dp)
            .padding(32.dp, 20.dp, 32.dp, 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val rs = LocalContext.current.resources

        Text(
            rs.getString(R.string.operation),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
        )

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
            rs.getString(R.string.attention),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
        )
        Text(
            rs.getString(R.string.attention_content),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(0.dp, 2.5.dp, 0.dp, 2.5.dp)
        )
    }

}

@Composable
fun PageFooter() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black, fontSize = 12.sp)) {
                append("Github")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://github.com/GuoXiCheng/SKIP",
                    start = 0,
                    end = 6
                )
                append(" | ")
                append("Document")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://guoxicheng.github.io/SKIP-Docs/introduction",
                    start = 9,
                    end = 18
                )
                append(" | Version " + BuildConfig.VERSION_NAME)
            }
        }
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations("URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val url = annotation.item
                        coroutineScope.launch {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    }
            }
        )
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

@Composable
fun ImageDialog() {
    AlertDialog(
        onDismissRequest = {isBackendTaskBtnClicked= false },
        containerColor = Color.White,
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.backend_lock), // 设置要显示的图片
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
                    ) {
                TextButton(
                    onClick = { isBackendTaskBtnClicked = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = green)
                ) {
                    Text(text = "我知道了")
                }
            }

        }
    )
}



