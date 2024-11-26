package com.rohmanbeny.mov.sign.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rohmanbeny.mov.databinding.ActivitySignUpBinding
import com.rohmanbeny.mov.sign.signin.User
import com.rohmanbeny.mov.utils.Preferences

class SignUpActivity : AppCompatActivity() {

    private lateinit var sUsername: String
    private lateinit var sPassword: String
    private lateinit var sNama: String
    private lateinit var sEmail: String
    private lateinit var sSaldo: String
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnLanjutkan.setOnClickListener {
            sUsername = binding.etUsername.text.toString()
            sPassword = binding.etPassword.text.toString()
            sNama = binding.etNama.text.toString()
            sEmail = binding.etEmail.text.toString()
            sSaldo = "200000"

            when {
                sUsername.isEmpty() -> {
                    binding.etUsername.error = "Username wajib diisi"
                    binding.etUsername.requestFocus()
                }
                sPassword.isEmpty() -> {
                    binding.etPassword.error = "Password wajib diisi"
                    binding.etPassword.requestFocus()
                }
                sNama.isEmpty() -> {
                    binding.etNama.error = "Nama wajib diisi"
                    binding.etNama.requestFocus()
                }
                sEmail.isEmpty() -> {
                    binding.etEmail.error = "Email wajib diisi"
                    binding.etEmail.requestFocus()
                }
                sUsername.contains(".") -> {
                    binding.etUsername.error = "Silahkan tulis Username Anda tanpa ."
                    binding.etUsername.requestFocus()
                }
                else -> {
                    checkUsernameAvailability(sUsername, sNama, sEmail, sPassword, sSaldo)
                }
            }
        }
    }

    private fun checkUsernameAvailability(
        username: String,
        name: String,
        email: String,
        password: String,
        saldo: String
    ) {
        mFirebaseDatabase.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.etUsername.error = "Username sudah terdaftar"
                    binding.etUsername.requestFocus()
                } else {
                    saveUserToDatabase(username, name, email, password, saldo)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserToDatabase(
        username: String,
        name: String,
        email: String,
        password: String,
        saldo: String) {

        val user = User(
            uid = username,          // UID
            nama = name,             // Nama lengkap
            username = username,     // Username
            saldo = saldo,           // Saldo awal
            url = "Empty",           // URL profil default kosong
            email = email,           // Email
            statue = "Verify",       // Status default
            password = password      // Password
        )

        mFirebaseDatabase.child(username).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    preferences.setValues("uid", username)
                    preferences.setValues("nama", name)
                    preferences.setValues("user", username)
                    preferences.setValues("saldo", saldo)
                    preferences.setValues("email", email)
                    preferences.setValues("password", password)

                    val intent = Intent(this@SignUpActivity, SignUpPhotoScreenActivity::class.java)
                    intent.putExtra("data", user)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Gagal menyimpan data pengguna: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
