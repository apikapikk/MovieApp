package com.rohmanbeny.mov.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivitySuccesMywalletBinding
import com.rohmanbeny.mov.home.HomeActivity

class SuccesTopUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccesMywalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding untuk mengikat layout
        binding = ActivitySuccesMywalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menangani klik pada tombol home
        binding.btnHome.setOnClickListener {
            finishAffinity() // Menutup semua activity sebelumnya
            val intent = Intent(this@SuccesTopUpActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
