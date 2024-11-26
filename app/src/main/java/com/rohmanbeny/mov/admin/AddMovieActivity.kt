package com.rohmanbeny.mov.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.rohmanbeny.mov.databinding.ActivityAddMovieBinding
import com.rohmanbeny.mov.model.Film

class AddMovieActivity : AppCompatActivity() {

    // ViewBinding instance
    private lateinit var binding: ActivityAddMovieBinding

    // Inisialisasi Firebase Firestore
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set aksi tombol Add
        binding.addBtnAddMovie.setOnClickListener {
            saveFilmToFirestore()
        }
    }

    private fun saveFilmToFirestore() {
        // Ambil data dari input user menggunakan binding
        val judul = binding.addEtMovieTitle.text.toString().trim()
        val desc = binding.addEtMovieDesc.text.toString().trim()
        val director = binding.addEtMovieDirector.text.toString().trim()
        val genre = binding.addEtMovieGenre.text.toString().trim()
        val poster = binding.addEtMoviePoster.text.toString().trim()
        val rating = binding.addEtMovieRating.text.toString().trim()

        // Validasi input
        if (judul.isEmpty() || desc.isEmpty() || director.isEmpty() || genre.isEmpty() || poster.isEmpty() || rating.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek Film tanpa idFilm karena Firebase akan generate ID otomatis
        val film = Film(
            idFilm = "", // Kosongkan, Firebase akan menggenerate ID secara otomatis
            desc = desc,
            director = director,
            genre = genre,
            judul = judul,
            poster = poster,
            rating = rating
        )

        // Simpan ke Firestore
        db.collection("films")
            .add(film)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id
                film.idFilm = generatedId
                 db.collection("films").document(generatedId).set(film)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Film berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        // Opsional: Reset form setelah sukses
                        resetForm()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan film: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menambahkan film: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resetForm() {
        with(binding) {
            addEtMovieTitle.text.clear()
            addEtMovieDesc.text.clear()
            addEtMovieDirector.text.clear()
            addEtMovieGenre.text.clear()
            addEtMoviePoster.text.clear()
            addEtMovieRating.text.clear()
        }
    }
}
