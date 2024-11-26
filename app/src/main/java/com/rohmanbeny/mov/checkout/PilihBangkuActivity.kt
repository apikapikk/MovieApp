package com.rohmanbeny.mov.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivityPilihBangkuBinding
import com.rohmanbeny.mov.model.Checkout
import com.rohmanbeny.mov.model.Film
import kotlin.reflect.KMutableProperty0

class PilihBangkuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPilihBangkuBinding

    var statusA3: Boolean = false
    var statusA4: Boolean = false
    var statusB1: Boolean = false
    var statusC3: Boolean = false
    var statusC4: Boolean = false
    var statusD1: Boolean = false
    var statusD2: Boolean = false
    var statusD3: Boolean = false
    var statusD4: Boolean = false
    var total: Int = 0

    private var datalist = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihBangkuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Film>("data")
        binding.tvKursi.text = data?.judul

        setupSeatListeners()

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
                .putParcelableArrayListExtra("data", datalist)
                .putExtra("datas", data)
            startActivity(intent)
        }

        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    private fun setupSeatListeners() {
        binding.a3.setOnClickListener { toggleSeat("A3", 50000, binding.a3, ::statusA3) }
        binding.a4.setOnClickListener { toggleSeat("A4", 50000, binding.a4, ::statusA4) }
        binding.b1.setOnClickListener { toggleSeat("B1", 47000, binding.b1, ::statusB1) }
        binding.c3.setOnClickListener { toggleSeat("C3", 45000, binding.c3, ::statusC3) }
        binding.c4.setOnClickListener { toggleSeat("C4", 45000, binding.c4, ::statusC4) }
        binding.d1.setOnClickListener { toggleSeat("D1", 40000, binding.d1, ::statusD1) }
        binding.d2.setOnClickListener { toggleSeat("D2", 40000, binding.d2, ::statusD2) }
        binding.d3.setOnClickListener { toggleSeat("D3", 40000, binding.d3, ::statusD3) }
        binding.d4.setOnClickListener { toggleSeat("D4", 40000, binding.d4, ::statusD4) }
    }

    private fun toggleSeat(seat: String, price: Int, view: View, status: KMutableProperty0<Boolean>) {
        if (status.get()) {
            view.setBackgroundResource(R.drawable.ic_rectangle_empty)
            total -= 1
            datalist.removeIf { it.kursi == seat }
            status.set(false)
        } else {
            view.setBackgroundResource(R.drawable.ic_rectangle_selected)
            total += 1
            // Menambahkan objek Checkout dengan harga yang benar
            datalist.add(Checkout(seat, price.toString())) // Pastikan harga adalah tipe String
            status.set(true)
        }
        updateBuyButton()
    }

    private fun updateBuyButton() {
        if (total == 0) {
            binding.btnHome.text = "Beli tiket"
            binding.btnHome.visibility = View.INVISIBLE
        } else {
            binding.btnHome.text = "Beli tiket ($total)"
            binding.btnHome.visibility = View.VISIBLE
        }
    }
}
