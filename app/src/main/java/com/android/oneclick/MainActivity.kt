package com.android.oneclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.android.oneclick.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var accessibilityEnabled = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window.setBackgroundDrawable(resources.getDrawable(R.drawable.simple, null))

        binding.button.setOnClickListener {
            if (accessibilityEnabled != 1) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                this.startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accessibilityEnabled = Settings.Secure.getInt(
            this.applicationContext.contentResolver,Settings.Secure.ACCESSIBILITY_ENABLED)
        if (accessibilityEnabled == 1) binding.button.text = resources.getString(R.string.enabled)
    }
}