package com.rohmanbeny.mov

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rohmanbeny.mov.databinding.ActivitySplashScreenBinding
import com.rohmanbeny.mov.onboarding.OnboardingOneActivity

/*
    This is Splash Screen
*/

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding untuk mengikat layout
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handler untuk delay dan navigasi ke OnboardingOneActivity
        Handler(mainLooper).postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000) // Delay 4 detik
    }
}
