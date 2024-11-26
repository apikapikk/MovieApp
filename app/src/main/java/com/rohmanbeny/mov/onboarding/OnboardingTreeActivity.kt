package com.rohmanbeny.mov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rohmanbeny.mov.databinding.ActivityOnboardingTreeBinding
import com.rohmanbeny.mov.sign.signin.SignInActivity

class OnboardingTreeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnboardingTreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingTreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener {
            finishAffinity()
            var intent = Intent(this@OnboardingTreeActivity,
            SignInActivity::class.java)
            startActivity(intent)
        }
    }
}