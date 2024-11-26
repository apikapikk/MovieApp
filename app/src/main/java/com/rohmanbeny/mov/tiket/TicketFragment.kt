package com.rohmanbeny.mov.tiket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rohmanbeny.mov.databinding.FragmentTicketBinding
import com.rohmanbeny.mov.model.Checkout
import com.rohmanbeny.mov.utils.Preferences

class TicketFragment : Fragment() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase: FirebaseFirestore
    private var datalist = ArrayList<Checkout>()

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireContext())
        val uid = preferences.getValues("uid").toString()

        // Log UID untuk memastikan apakah benar UID yang diperoleh
        Log.d("TicketFragment", "UID: $uid")

        if (uid.isEmpty()) {
            Toast.makeText(context, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        // Inisialisasi Firestore
        mDatabase = FirebaseFirestore.getInstance()

        binding.rcTiket.layoutManager = LinearLayoutManager(context)

        // Ambil data berdasarkan UID
        getData(uid)
    }

    private fun getData(uid: String) {
        // Membuat query untuk mencari transaksi berdasarkan idPembeli
        mDatabase.collection("transaksi")
            .whereEqualTo("idPembeli", uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents != null && !documents.isEmpty) {
                        datalist.clear()
                        // Looping untuk mengambil data transaksi
                        for (document in documents) {
                            val checkout = document.toObject(Checkout::class.java)
                            datalist.add(checkout)
                        }

                        // Update RecyclerView setelah data berhasil didapatkan
                        binding.rcTiket.adapter = TiketAdapter(datalist) { checkout ->
                            val intent = Intent(context, TiketActivity::class.java)
                                .putExtra("data", checkout)
                            startActivity(intent)
                        }

                        // Update total tiket
                        binding.tvTotal.text = "${datalist.size} Tickets"

                        // Jika data kosong
                        if (datalist.isEmpty()) {
                            Toast.makeText(context, "No tickets available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("TicketFragment", "No data found for UID: $uid")
                        Toast.makeText(context, "No tickets available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TicketFragment", "Error getting documents: ", task.exception)
                    Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
