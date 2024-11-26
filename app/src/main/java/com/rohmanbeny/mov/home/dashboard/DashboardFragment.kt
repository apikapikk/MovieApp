package com.rohmanbeny.mov.home.dashboard
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.rohmanbeny.mov.detail.DetailActivity
import com.rohmanbeny.mov.databinding.FragmentDashboardBinding
import com.rohmanbeny.mov.model.Film
import com.rohmanbeny.mov.utils.Preferences
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences
    private lateinit var firestore: FirebaseFirestore

    private var datalist = ArrayList<Film>()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Menggunakan binding untuk menginflasi layout
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = Preferences(requireActivity().applicationContext)
        firestore = FirebaseFirestore.getInstance()  // Menggunakan Firestore

        binding.tvNama.text = preferences.getValues("nama")

        if (!preferences.getValues("saldo").isNullOrEmpty()) {
            curency(preferences.getValues("saldo")!!.toDouble(), binding.tvSaldo)
        } else {
            binding.tvSaldo.text = "Duit ane kosong :("
        }

        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfile)

        binding.nowPlaying.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvComingSoon.layoutManager = LinearLayoutManager(context)

        getData()
    }

    private fun getData() {
        firestore.collection("films")  // Gantilah dengan nama koleksi Anda di Firestore
            .get()
            .addOnSuccessListener { result ->
                datalist.clear()
                if (result.isEmpty) {
                    Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show()
                }
                for (document in result) {
                    val film = document.toObject(Film::class.java)  // Mengonversi dokumen ke objek Film
                    datalist.add(film)
                }
                // Cek apakah data ada di adapter
                Log.d("DashboardFragment", "Jumlah data yang diterima: ${datalist.size}")

                binding.nowPlaying.adapter = NowPlayingAdapter(datalist) {
                    val intent = Intent(context, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }
                binding.rvComingSoon.adapter = ComingSoonAdapter(datalist) {
                    val intent = Intent(context, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                Log.e("DashboardFragment", "Error getting documents: $e")
                Toast.makeText(context, "Error getting documents: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun curency(harga: Double, textView: TextView) {
        val localID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localID)
        textView.text = format.format(harga)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
