package com.android.oneclick

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.android.oneclick.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var accessibilityEnabled = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window.setBackgroundDrawable(resources.getDrawable(R.drawable.summer_background, null))
        this.window.navigationBarColor = resources.getColor(R.color.teal_500, null)

        binding.switchButton.setOnClickListener {
            if (binding.switchButton.isChecked && accessibilityEnabled != 1) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                this.startActivity(intent)
            }
            binding.switchButton.isChecked = accessibilityEnabled == 1
        }
    }

    override fun onResume() {
        super.onResume()
        accessibilityEnabled = Settings.Secure.getInt(
            this.applicationContext.contentResolver,Settings.Secure.ACCESSIBILITY_ENABLED)
        binding.switchButton.isChecked = accessibilityEnabled == 1
    }
}