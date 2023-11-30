package com.android.skip

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.skip.compose.StartButton
import com.android.skip.viewmodel.StartButtonViewModel

class NewMainActivity : AppCompatActivity() {
    private val startButtonViewModel: StartButtonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(vertical = 64.dp, horizontal = 32.dp)
            ) {
                StartButton(startButtonViewModel)
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