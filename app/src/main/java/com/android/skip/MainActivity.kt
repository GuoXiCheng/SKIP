package com.android.skip

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.android.skip.dataclass.PackageInfo
import com.android.skip.manager.*
import com.android.skip.ui.theme.OneClickTheme
import com.android.skip.ui.theme.green
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.roundToInt


var accessibilityState by mutableStateOf(false)

var alertDialogPositiveButtonClickState by mutableStateOf(false)

// 无障碍服务启用停用按钮
var isAccessibilityBtnClicked by mutableStateOf(false)

// 后台任务管理
var isBackendTaskBtnClicked by mutableStateOf(false)

// 自启动管理
var isAutoStartBtnClicked by mutableStateOf(false)

// 省电策略按钮
var isPowerSavingBtnClicked by mutableStateOf(false)

// 检查更新按钮
var isCheckUpdateBtnClicked by mutableStateOf(false)

// 是否需要更新
var isNeedUpdateAPK by mutableStateOf(false)

// 最新版本号
var latestVersionText by mutableStateOf("")

// 立即更新
var isUpdateAPKClicked by mutableStateOf(false)

// 更新进度对话框
var isProcessDialogVisible by mutableStateOf(false)

var downloadProgress by mutableStateOf(0f)


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
                    intent.component = ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                }
                manufacturer.contains("oppo") -> {
                    intent.component = ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
                }
                manufacturer.contains("vivo") -> {
                    intent.component = ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                }
                manufacturer.contains("oneplus") -> {
                    intent.component = ComponentName(
                        "com.oneplus.security",
                        "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"
                    )
                }
                manufacturer.contains("huawei") -> {
                    intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                    )
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

            val displayMetrics = LocalContext.current.resources.displayMetrics
            val widthPixels = displayMetrics.widthPixels
            val fontScale = LocalDensity.current.fontScale

            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = widthPixels / 360.0f,
                    fontScale = fontScale
                )
            ) {
                MainSurface()
                ProcessDialog()
            }


            if (isAccessibilityBtnClicked) {
                if (!accessibilityState) {
                    AlertDialog(
                        context = this,
                        title = "启用无障碍服务",
                        message = "已下载的应用 > SKIP > 打开「使用SKIP」",
                        negativeText = "再想想",
                        positiveText = "去开启"
                    ) {
                        alertDialogPositiveButtonClickState = true
                    }
                } else {
                    AlertDialog(
                        context = this,
                        title = "停用无障碍服务",
                        message = "已下载的应用 > SKIP > 关闭「使用SKIP」",
                        negativeText = "再想想",
                        positiveText = "去停用"
                    ) {
                        alertDialogPositiveButtonClickState = true
                    }
                }
                isAccessibilityBtnClicked = false
            }

            if (isNeedUpdateAPK) {
                AlertDialog(
                    context = this,
                    title = "发现新版本",
                    message = "${BuildConfig.VERSION_NAME} -> $latestVersionText",
                    negativeText = "暂不更新",
                    positiveText = "立即更新"
                ) {
                    isUpdateAPKClicked = true
                }
                isNeedUpdateAPK = false
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
                isCheckUpdateBtnClicked -> {
                    thread {
                        ToastManager.showToast(this, "开始检查更新")
                        val updateSkipConfigResult =
                            if (HttpManager.updateSkipConfig()) "配置更新成功" else "配置更新失败"
                        ToastManager.showToast(this, updateSkipConfigResult)

                        val latestVersion = HttpManager.getLatestVersion()
                        if (latestVersion != BuildConfig.VERSION_NAME.trim()) {
                            isNeedUpdateAPK = true
                            latestVersionText = latestVersion
                        } else {
                            ToastManager.showToast(this, "当前版本已是最新版")
                        }

                        isCheckUpdateBtnClicked = false
                    }
                }
                isUpdateAPKClicked -> {
                    thread {
                        isProcessDialogVisible = true
                        HttpManager.downloadNewAPK(latestVersionText, this) { it ->
                            downloadProgress = it * 0.01f
                            if (it == 100) isProcessDialogVisible = false
                        }
                        val latestVersionAPK = "SKIP-v$latestVersionText.apk"
                        val apkFile = File(this.getExternalFilesDir(null), latestVersionAPK)

                        val apkUri = FileProvider.getUriForFile(
                            this,
                            this.applicationContext.packageName + ".provider",
                            apkFile
                        )

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        this.startActivity(intent)

                        isUpdateAPKClicked = false
                    }
                }
            }

        }

        RectManager.setMaxRect(this)

        val yaml = Yaml().load<List<PackageInfo>>(assets.open("skip_config.yaml"))
        SkipConfigManager.setConfig(yaml)

        // 清理临时文件
        val directory = this.getExternalFilesDir(null)
        directory?.let {
            it.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accessibilityState = MyUtils.isAccessibilitySettingsOn(this)
    }
}


@Composable
@Preview(showBackground = true)
fun MainSurface() {
    OneClickTheme {

        val res = LocalContext.current.resources

        PageHeader(res.getString(R.string.app_name), "是一款免费开源的自动跳过开屏广告的工具")


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.offset(y = (-50).dp)) {
                AccessibilityControlBtn()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.offset(y = (90).dp)) {
                AccessibilityTextBox()
            }
        }

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
            .padding(top = 60.dp, start = 20.dp)
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
    Row {
        if (accessibilityState) {
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
            .height(240.dp)
            .fillMaxWidth()
            .padding(start = 15.dp, top = 10.dp)
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
fun PageFooter() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
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
                append(RectManager.getMaxRect())
                append(" | version " + BuildConfig.VERSION_NAME)
                append(" | ")
                append("检查更新")
                addStringAnnotation(
                    tag = "CHECK_UPDATE",
                    annotation = "",
                    start = length - 4,
                    end = length
                )
            }
        }
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(start = offset, end = offset).firstOrNull()
                    ?.let { annotation ->
                        when (annotation.tag) {
                            "URL" -> {
                                val url = annotation.item
                                coroutineScope.launch {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            }
                            "CHECK_UPDATE" -> {
                                isCheckUpdateBtnClicked = true
                            }
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
    positiveText: CharSequence, onPositiveButtonClick: () -> Unit
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(negativeText, null)
        .setPositiveButton(positiveText) { _, _ ->
            onPositiveButtonClick()
        }
        .show()
}

@Composable
fun ImageDialog() {
    AlertDialog(
        onDismissRequest = { isBackendTaskBtnClicked = false },
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
            Row(
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

@Composable
fun ProcessDialog() {
    if (isProcessDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                isProcessDialogVisible = false
            },
            title = {
                Text(text = "正在下载")
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    LinearProgressIndicator(progress = downloadProgress)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "下载进度：${(downloadProgress * 100).roundToInt()}%")
                }
            },
            confirmButton = {
            },
            dismissButton = {
            }
        )
    }
}


