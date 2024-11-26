package com.rohmanbeny.mov.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.rohmanbeny.mov.checkout.PilihBangkuActivity
import com.rohmanbeny.mov.databinding.ActivityDetailBinding
import com.rohmanbeny.mov.home.dashboard.PlaysAdapter
import com.rohmanbeny.mov.model.Film
import com.rohmanbeny.mov.model.Plays


class DetailActivity : AppCompatActivity() {

    private lateinit var mDatabase : DatabaseReference
    private var datalist = ArrayList<Plays> ()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Film>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
            .child(data!!.judul.toString())
            .child("play")

        binding.tvKursi.text = data.judul
        binding.tvGenre.text = data.genre
        binding.tvDesc.text = data.desc
        binding.tvRate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(binding.ivPoster)

        binding.rvWhoPlay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()

        binding.btnPilihBangku.setOnClickListener{
            var intent = Intent(this@DetailActivity, PilihBangkuActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }
        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                datalist.clear()

                for (getdataSnapshot in p0.children) {
                    var Film = getdataSnapshot.getValue(Plays::class.java)
                    datalist.add(Film!!)
                }
                binding.rvWhoPlay.adapter = PlaysAdapter(datalist){

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@DetailActivity,""+p0.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}