package com.rohmanbeny.mov.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivityCheckoutBinding
import com.rohmanbeny.mov.model.Checkout
import com.rohmanbeny.mov.model.Film
import com.rohmanbeny.mov.tiket.TiketActivity
import com.rohmanbeny.mov.utils.Preferences
import java.text.NumberFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var datalist = ArrayList<Checkout>()
    private var total: Int = 0
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        // Get data from intent
        try {
            datalist = intent.getParcelableArrayListExtra("data") ?: ArrayList()
            Log.d("CheckoutActivity", "Datalist: $datalist")
        } catch (e: Exception) {
            Log.e("CheckoutActivity", "Error parsing datalist from intent", e)
        }

        val data: Film? = try {
            intent.getParcelableExtra("datas")
        } catch (e: Exception) {
            Log.e("CheckoutActivity", "Error parsing Film data from intent", e)
            null
        }
        Log.d("CheckoutActivity", "Film data: $data")

        // Calculate total
        for (checkout in datalist) {
            total += checkout.harga?.toIntOrNull() ?: 0
        }
        datalist.add(Checkout("Total harus dibayar", harga = total.toString()))

        // Setup RecyclerView
        binding.rcCheckout.layoutManager = LinearLayoutManager(this)
        binding.rcCheckout.adapter = CheckoutAdapter(datalist) {}

        // Handle buttons
        binding.btnTiket.setOnClickListener {
            if (data != null) {
                // Cek saldo
                val saldo = preferences.getValues("saldo")?.toDoubleOrNull() ?: 0.0

                if (saldo >= total) {
                    // Mengurangi saldo
                    val newSaldo = saldo - total
                    preferences.setValues("saldo", newSaldo.toString()) // Simpan saldo baru

                    // Simpan data transaksi di Firestore
                    val firestore = FirebaseFirestore.getInstance()

                    // Ambil waktu transaksi
                    val currentDate = System.currentTimeMillis().toString()

                    // Membuat objek Checkout
                    val checkout = Checkout(
                        kursi = datalist.joinToString(", ") { it.kursi },
                        harga = total.toString(),
                        idTransaksi = UUID.randomUUID().toString(),
                        idPembeli = preferences.getValues("user") ?: "unknown",
                        namaPembeli = preferences.getValues("nama") ?: "unknown",
                        judulFilm = data.judul,
                        idFilm = data.idFilm,
                        tanggalPembelian = currentDate
                    )

                    // Simpan transaksi ke Firestore
                    firestore.collection("transaksi")
                        .add(checkout)
                        .addOnSuccessListener {
                            Log.d("CheckoutActivity", "Transaksi berhasil disimpan di Firestore.")
                            // Kirim notifikasi
                            showNotif(data)

                            // Pindah ke TiketActivity
                            val intent = Intent(this, TiketActivity::class.java)
                            intent.putExtra("data", data)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.e("CheckoutActivity", "Error menyimpan transaksi ke Firestore", e)
                        }
                } else {
                    // Tampilkan pesan saldo tidak cukup
                    binding.textView42.visibility = View.VISIBLE
                    binding.textView42.text = "Saldo pada e-wallet kamu tidak mencukupi\nuntuk melakukan transaksi"
                }
            }
        }

        binding.btnHome.setOnClickListener {
            finish()
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

        // Handle saldo
        val saldo = preferences.getValues("saldo")?.toDoubleOrNull() ?: 0.0
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        binding.tvSaldo.text = formatRupiah.format(saldo)

        if (saldo >= total) {
            binding.btnTiket.visibility = View.VISIBLE
            binding.textView42.visibility = View.INVISIBLE
        } else {
            binding.btnTiket.visibility = View.INVISIBLE
            binding.textView42.visibility = View.VISIBLE
            binding.textView42.text = "Saldo pada e-wallet kamu tidak mencukupi\n" +
                    "untuk melakukan transaksi"
        }

        Log.d("CheckoutActivity", "Saldo: $saldo, Total: $total")
    }

    private fun showNotif(data: Film) {
        val NOTIFICATION_CHANNEL_ID = "channel_mov_notif"
        val context = this.applicationContext
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "MOV Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        /*
        val intent = Intent(this, TiketActivity::class.java).apply {
            putExtra("data", data)
        }*/

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.logo_mov)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.logo_notification
                )
            )
            .setTicker("notif mov starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Sukses Terbeli")
            .setContentText("Tiket ${data.judul} berhasil kamu dapatkan. Enjoy the movie!")

        notificationManager.notify(115, builder.build())
    }
}
