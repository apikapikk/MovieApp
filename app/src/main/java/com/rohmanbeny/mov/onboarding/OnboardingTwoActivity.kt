package com.rohmanbeny.mov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rohmanbeny.mov.databinding.ActivityOnboardingTwoBinding

class OnboardingTwoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this@OnboardingTwoActivity,
                OnboardingTreeActivity::class.java))
        }
    }
}