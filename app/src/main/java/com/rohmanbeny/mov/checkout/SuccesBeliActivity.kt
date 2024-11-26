package com.rohmanbeny.mov.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohmanbeny.mov.databinding.ActivitySuccesBeliBinding
import com.rohmanbeny.mov.home.HomeActivity

class SuccesBeliActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccesBeliBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccesBeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}