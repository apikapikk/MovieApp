package com.rohmanbeny.mov.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    var idFilm: String = "",
    var desc: String = "",
    var director: String = "",
    var genre: String = "",
    var judul: String = "",
    var poster: String = "",
    var rating: String = ""
) : Parcelable {
    // describeContents() tidak perlu ditulis jika Anda menggunakan @Parcelize
    // karena annotation ini akan otomatis menghasilkan implementasi Parcelable
}
