package com.rohmanbeny.mov.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivityMyWalletTopUpBinding
import com.rohmanbeny.mov.utils.Preferences
import java.text.NumberFormat
import java.util.*

class MyWalletTopUpActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var binding: ActivityMyWalletTopUpBinding

    private var selectedNominal: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyWalletTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        // Menampilkan saldo dari Preferences
        val currentSaldo = preferences.getValues("saldo")?.toDoubleOrNull() ?: 0.0
        curency(currentSaldo, binding.tvSaldo)

        // Event tombol Top-Up
        binding.btnTopUp.setOnClickListener {
            val customNominal = binding.etTopup.text.toString().toDoubleOrNull()
            val topUpAmount = selectedNominal ?: customNominal

            if (topUpAmount != null && topUpAmount > 0) {
                updateSaldo(topUpAmount)
            } else {
                Toast.makeText(this, "Silakan pilih nominal atau masukkan jumlah top-up", Toast.LENGTH_LONG).show()
            }
        }

        // Event tombol Close
        binding.ivClose.setOnClickListener {
            finish()
        }

        // Event klik untuk memilih nominal top-up
        binding.tv1k.setOnClickListener { selectNominal(1000.0, binding.tv1k) }
        binding.tv2k.setOnClickListener { selectNominal(2000.0, binding.tv2k) }
        binding.tv3k.setOnClickListener { selectNominal(3000.0, binding.tv3k) }
        binding.tv4k.setOnClickListener { selectNominal(4000.0, binding.tv4k) }
        binding.tv5k.setOnClickListener { selectNominal(5000.0, binding.tv5k) }
        binding.tv6k.setOnClickListener { selectNominal(6000.0, binding.tv6k) }

        // Hapus pilihan saat mengetik nominal custom
        binding.etTopup.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) clearNominalSelection()
        }
    }

    private fun selectNominal(amount: Double, textView: TextView) {
        clearNominalSelection()
        selectedNominal = amount
        textView.setTextColor(resources.getColor(R.color.colorBlue))
        textView.setBackgroundResource(R.drawable.shape_line_blue)
        binding.etTopup.text.clear() // Clear custom nominal text when selecting predefined value
        binding.btnTopUp.visibility = View.VISIBLE // Show the top-up button
    }

    private fun clearNominalSelection() {
        selectedNominal = null
        resetTextView(binding.tv1k)
        resetTextView(binding.tv2k)
        resetTextView(binding.tv3k)
        resetTextView(binding.tv4k)
        resetTextView(binding.tv5k)
        resetTextView(binding.tv6k)
        binding.btnTopUp.visibility = View.GONE // Hide the top-up button when no amount is selected
    }

    private fun resetTextView(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.shape_line_white)
    }

    private fun updateSaldo(topUpAmount: Double) {
        val userId = preferences.getValues("uid")
        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("User").child(userId)

            // Ambil saldo saat ini dari Firebase
            userRef.get().addOnSuccessListener { snapshot ->
                val currentSaldo = snapshot.child("saldo").value.toString().toDoubleOrNull() ?: 0.0
                val updatedSaldo = currentSaldo + topUpAmount

                // Update saldo di Firebase
                userRef.child("saldo").setValue(updatedSaldo).addOnSuccessListener {
                    preferences.setValues("saldo", updatedSaldo.toString())
                    curency(updatedSaldo, binding.tvSaldo)

                    // Pindah ke halaman sukses
                    startActivity(Intent(this, SuccesTopUpActivity::class.java))
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengupdate saldo: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengambil data pengguna: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "User ID tidak ditemukan", Toast.LENGTH_LONG).show()
        }
    }

    private fun curency(harga: Double, textView: TextView) {
        val localID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localID)
        textView.text = format.format(harga)
    }
}
