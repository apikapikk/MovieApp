package com.rohmanbeny.mov.tiket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rohmanbeny.mov.databinding.ActivityTiketBinding
import com.rohmanbeny.mov.model.Checkout
import com.rohmanbeny.mov.model.Film

class TiketActivity : AppCompatActivity() {

    private var datalist = arrayListOf<Checkout>()
    private lateinit var binding: ActivityTiketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filmId = intent.getStringExtra("FILM_ID")
        val data = intent.getParcelableExtra<Film>("data")

        if (filmId != null) {
            Log.d("TiketActivity", "F2 Film dengan ID $filmId tidak ditemukan!")
            val db = FirebaseFirestore.getInstance()
            val filmRef = db.collection("films") // Koleksi film di Firestore
            val query: Query = filmRef.whereEqualTo("idFilm", filmId) // Filter berdasarkan ID film
            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Jika data ditemukan
                        val film = documents.documents.firstOrNull()?.toObject(Film::class.java)
                        if (film != null) {
                            // Menampilkan detail film
                            binding.tvTitle.text = film.judul
                            binding.tvGenre.text = film.genre
                            binding.tvRate.text = film.rating

                            Log.d("TiketActivity", "F3 Film dengan ID $filmId tidak ditemukan!")
                            Glide.with(this@TiketActivity)
                                .load(film.poster)
                                .into(binding.ivPoster)
                        }
                    } else {
                        // Jika film tidak ditemukan
                        Log.d("TiketActivity", "F4 Film dengan ID $filmId tidak ditemukan!")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TiketActivity", "Error getting documents: ${exception.message}")
                }
        }

        // Menampilkan detail film
        binding.tvTitle.text = data?.judul
        binding.tvGenre.text = data?.genre
        binding.tvRate.text = data?.rating

        Glide.with(this)
            .load(data?.poster)
            .into(binding.ivPoster)

        // Ambil data checkout terkait dari Firebase
        val uid = "uid" // Ganti dengan UID yang relevan
        val transaksiRef = FirebaseDatabase.getInstance().getReference("transaksi").child(uid)

        transaksiRef.child(data?.judul ?: "").child("checkout").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                datalist.clear()
                for (checkoutSnapshot in snapshot.children) {
                    val checkout = checkoutSnapshot.getValue(Checkout::class.java)
                    if (checkout != null) {
                        datalist.add(checkout)
                    }
                }

                // Set adapter
                binding.rcCheckout.layoutManager = LinearLayoutManager(this@TiketActivity)
                binding.rcCheckout.adapter = TiketAdapter(datalist) {
                    // Handle item click if needed
                }
            }

            override fun onCancelled(error: DatabaseError) {
                finish()
            }
        })

        binding.ivClose.setOnClickListener { finish() }
        binding.ivBarcode.setOnClickListener {
            showDialog("Silahkan melakukan scanning pada counter tiket terdekat")
        }
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.rohmanbeny.mov.R.layout.dialog_qr)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvDesc = dialog.findViewById<TextView>(com.rohmanbeny.mov.R.id.tv_desc)
        tvDesc.text = title

        val btnClose = dialog.findViewById<Button>(com.rohmanbeny.mov.R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

