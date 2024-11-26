package com.rohmanbeny.mov.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohmanbeny.mov.databinding.ActivityOnboardingOneBinding
import com.rohmanbeny.mov.sign.signin.SignInActivity
import com.rohmanbeny.mov.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preference:Preferences
    private lateinit var binding: ActivityOnboardingOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = Preferences(this)

        if (preference.getValues("onboarding").equals("1")) {
            finishAffinity()

            var intent = Intent(this@OnboardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            var intent = Intent(this@OnboardingOneActivity,
                OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            preference.setValues("onboarding", "1")
            finishAffinity()

            var intent = Intent(this@OnboardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}