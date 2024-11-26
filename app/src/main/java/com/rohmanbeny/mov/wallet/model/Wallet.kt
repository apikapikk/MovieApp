package com.rohmanbeny.mov.wallet.model

import android.os.Parcelable

data class Wallet(
    var title: String = "",
    var date: String = "",
    var money: Double = 0.0,
    var status: String = ""
)
