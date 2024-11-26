package com.rohmanbeny.mov.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Checkout(
    var kursi: String = "",
    var harga: String = "",
    var idTransaksi: String = "",
    var idPembeli: String = "",
    var namaPembeli: String = "",
    var judulFilm: String = "",
    var idFilm: String = "",
    var tanggalPembelian: String = ""
) : Parcelable