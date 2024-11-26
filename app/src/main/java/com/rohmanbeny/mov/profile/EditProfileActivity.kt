package com.rohmanbeny.mov.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rohmanbeny.mov.databinding.ActivityEditProfileBinding
import com.rohmanbeny.mov.utils.Preferences

class EditProfileActivity : AppCompatActivity() {

    lateinit var preferences: Preferences
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        preferences = Preferences(this)

        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNama.text = preferences.getValues("nama")
        binding.tvUsername.text = preferences.getValues("user")
        binding.tvPassword.text = preferences.getValues("password")
        binding.tvEmail.text = preferences.getValues("email")

        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfileEdit)

        binding.ivClose.setOnClickListener {
            finish()
        }
    }
}