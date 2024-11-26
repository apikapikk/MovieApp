package com.rohmanbeny.mov.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivityMyWalletBinding
import com.rohmanbeny.mov.utils.Preferences
import com.rohmanbeny.mov.wallet.adaptor.WalletAdapter
import com.rohmanbeny.mov.wallet.model.Wallet
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyWalletActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var binding: ActivityMyWalletBinding
    private var dataList = ArrayList<Wallet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding untuk mengikat layout
        binding = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        // Menggunakan binding untuk mengakses view
        if (!preferences.getValues("saldo").equals("")) {
            curency(preferences.getValues("saldo")!!.toDouble(), binding.tvSaldo)
        } else {
            binding.tvSaldo.text = "Duit ane kosong :("
        }

        // Menambahkan data transaksi
        dataList.add(
            Wallet(
                "Spider Man",
                "Sabtu, 12 Des, 2021",
                800000.0,
                "0"
            )
        )
        dataList.add(
            Wallet(
                "Top Up",
                "Sabtu, 12 Des, 2021",
                1800000.0,
                "1"
            )
        )
        dataList.add(
            Wallet(
                "Spider Man",
                "Sabtu, 12 Des, 2021",
                800000.0,
                "0"
            )
        )

        // Menggunakan binding untuk RecyclerView dan Adapter
        binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
        binding.rvTransaksi.adapter = WalletAdapter(dataList) {
            // Handle item click if needed
        }

        // Menggunakan binding untuk menangani event klik tombol
        binding.btnTopUp.setOnClickListener {
            startActivity(Intent(this, MyWalletTopUpActivity::class.java))
        }

        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    private fun curency(harga: Double, textView: TextView) {
        val localID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localID)
        textView.text = format.format(harga)
    }
}
