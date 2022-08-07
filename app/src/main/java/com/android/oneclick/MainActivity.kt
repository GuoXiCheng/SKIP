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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.oneclick.ui.theme.OneClickTheme
import com.android.oneclick.ui.theme.Teal500

var accessibilityState by mutableStateOf(false)
class MainActivity : ComponentActivity() {
    private var accessibilityEnabled = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.teal_500, null)
        setContent {
            DefaultPreview(this)
        }

    }

    override fun onResume() {
        super.onResume()
        accessibilityEnabled = Settings.Secure.getInt(
            this.applicationContext.contentResolver,Settings.Secure.ACCESSIBILITY_ENABLED)
        accessibilityState = accessibilityEnabled == 1
    }
}

@Composable
fun DefaultPreview(context: Context) {
    OneClickTheme {
        Surface(modifier = Modifier.fillMaxSize(), color= Teal500) {

        }
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            },
            contentPadding = ButtonDefaults.TextButtonContentPadding,
            modifier = Modifier.absoluteOffset(120.dp, 700.dp)
        ) {
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            if(!accessibilityState) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "PlayArrow",
                    modifier = Modifier.size(ButtonDefaults.IconSize).offset((-5).dp,0.dp)
                )
                Text("启用无障碍服务")
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(ButtonDefaults.IconSize).offset((-5).dp,0.dp)
                )
                Text("停用无障碍服务")
            }

        }
    }
}

