package com.rohmanbeny.mov.sign.signin

import java.io.Serializable
data class User(
    val uid: String = "",
    val nama: String = "",
    val username: String = "",
    val saldo: String = "",
    val url: String = "",
    val email: String = "",
    val statue: String = "",
    val password: String = ""
) : Serializable
