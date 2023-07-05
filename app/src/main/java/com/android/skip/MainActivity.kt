package com.android.skip

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.ui.theme.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

var accessibilityState by mutableStateOf(false)
var accessibilityButtonClickState by mutableStateOf(false)
var alertDialogPositiveButtonClickState by mutableStateOf(false)
var expanded by mutableStateOf(false)
var selectedCurrentMobile by mutableStateOf(Mobile.XIAOMI.name)


class MainActivity : ComponentActivity() {

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
        accessibilityState = MyUtils.isAccessibilitySettingsOn(this)
    }
}


@Composable
@Preview
fun MainSurface() {
    OneClickTheme {

        val res = LocalContext.current.resources

        PageHeader(res.getString(R.string.app_name), "是一款免费开源的自动跳过APP开屏广告的工具")

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.offset(y = (-150).dp)) {
                ModelSelectionBtn()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AccessibilityControlBtn()
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp
            val screenHeightDp = configuration.screenHeightDp

            val xOffset = screenWidthDp / 2
            val yOffset = screenHeightDp / 2.5

            Box(modifier = Modifier.offset(x = (xOffset).dp - 70.dp, y = (yOffset).dp)) {
                ModelSelectionMenu(
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



        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            PromptBox()
            PageFooter()
        }

    }
}

@Composable
fun PageHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 60.dp)
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
fun AccessibilityControlBtn() {
    Button(
        onClick = {
            accessibilityButtonClickState = true
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Copyright © 2023 Your App. All rights reserved.",
            color = Color.Black,
            fontSize = 12.sp
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



