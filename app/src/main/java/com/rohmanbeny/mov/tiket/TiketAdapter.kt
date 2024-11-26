package com.rohmanbeny.mov.tiket

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.model.Checkout

class TiketAdapter(
    private val data: List<Checkout>,
    private val listener: (Checkout) -> Unit
) : RecyclerView.Adapter<TiketAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKursi: TextView = view.findViewById(R.id.tv_kursi)
        val tvHarga: TextView = view.findViewById(R.id.tv_harga)

        fun bind(item: Checkout, listener: (Checkout) -> Unit) {
            tvKursi.text = item.kursi
            tvHarga.text = "Rp ${item.harga}"
            itemView.setOnClickListener {
                // Menggunakan context dari itemView untuk Intent
                val intent = Intent(itemView.context, TiketActivity::class.java)
                // Kirim data Checkout ke ActivityTicket
                intent.putExtra("idFilm", item.idFilm)
                Log.d("TIkcetIdSending","Film ID: ${item.idFilm}")
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_checkout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size
}
