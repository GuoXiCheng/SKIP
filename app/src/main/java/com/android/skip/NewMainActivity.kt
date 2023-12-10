package com.android.skip

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.skip.compose.AboutButton
import com.android.skip.compose.CheckNewVersionButton
import com.android.skip.compose.ConfigUpdateButton
import com.android.skip.compose.SettingsButton
import com.android.skip.compose.StartButton
import com.android.skip.ui.theme.AppTheme
import com.android.skip.viewmodel.StartButtonViewModel

class NewMainActivity : AppCompatActivity() {
    private val startButtonViewModel: StartButtonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(darkTheme = true) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 64.dp, horizontal = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row {
                        Text(text = "SKIP", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    }
                    StartButton(startButtonViewModel)
                    ConfigUpdateButton()
                    SettingsButton()
                    CheckNewVersionButton()
                    AboutButton()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startButtonViewModel.changeButtonState(MyUtils.isAccessibilitySettingsOn(this))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}