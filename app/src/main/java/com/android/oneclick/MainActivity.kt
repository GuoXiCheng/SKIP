package com.android.oneclick

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.oneclick.ui.theme.OneClickTheme
import com.android.oneclick.ui.theme.Teal500
import com.android.oneclick.ui.theme.black
import com.android.oneclick.ui.theme.grey
import com.google.android.material.dialog.MaterialAlertDialogBuilder

var accessibilityState by mutableStateOf(false)
var accessibilityButtonClickState by mutableStateOf(false)
var alertDialogPositiveButtonClickState by mutableStateOf(false)
var expanded by mutableStateOf(false)
var selectedCurrentMobile by mutableStateOf(0)

class MainActivity : ComponentActivity() {
    private var accessibilityEnabled = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.teal_500, null)
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
                Text("当前手机", color = black)
                when (selectedCurrentMobile) {
                    0 -> Text("小米", color = grey)
                    1 -> Text("华为", color = grey)
                    2 -> Text("OPPO", color = grey)
                    3 -> Text("VIVO", color = grey)
                    4 -> Text("魅族", color = grey)
                    5 -> Text("其他手机", color = grey)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("小米") },
                    onClick = {
                        selectedCurrentMobile = 0
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text("华为") },
                    onClick = {
                        selectedCurrentMobile = 1
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text("OPPO") },
                    onClick = {
                        selectedCurrentMobile = 2
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text("VIVO") },
                    onClick = {
                        selectedCurrentMobile = 3
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text("魅族") },
                    onClick = {
                        selectedCurrentMobile = 4
                        expanded = false
                    })
                DropdownMenuItem(
                    text = { Text("其他手机") },
                    onClick = {
                        selectedCurrentMobile = 5
                        expanded = false
                    })
            }
        }
        when (selectedCurrentMobile) {
            0 -> Text("小米手机开启后台权限")
            1 -> Text("华为手机开启后台权限")
            2 -> Text("OPPO手机开启后台权限")
            3 -> Text("VIVO手机开启后台权限")
            4 -> Text("魅族手机开启后台权限")
            5 -> Text("其他手机开启后台权限")
        }

    }
}

@Composable
fun AccessibilityButton() {
    Button(
        onClick = {
            accessibilityButtonClickState = true
        },
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        modifier = Modifier.absoluteOffset(120.dp, 700.dp)
    ) {
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        if (!accessibilityState) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "PlayArrow",
                modifier = Modifier
                    .size(ButtonDefaults.IconSize)
                    .offset((-5).dp, 0.dp)
            )
            Text("启用无障碍服务")
        } else {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(ButtonDefaults.IconSize)
                    .offset((-5).dp, 0.dp)
            )
            Text("停用无障碍服务")
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



