package com.rohmanbeny.mov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rohmanbeny.mov.home.HomeActivity
import com.rohmanbeny.mov.databinding.ActivitySignInBinding
import com.rohmanbeny.mov.sign.signup.SignUpActivity
import com.rohmanbeny.mov.utils.Preferences

class SignInActivity : AppCompatActivity() {

    lateinit var iUsername :String
    lateinit var iPassword :String
    private lateinit var binding : ActivitySignInBinding
    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@SignInActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            iUsername = binding.etUsername.text.toString()
            iPassword = binding.etPassword.text.toString()

            if (iUsername.equals("")) {
                binding.etUsername.error = "Silahkan tulis Username Anda"
                binding.etUsername.requestFocus()
            } else if (iPassword.equals("")) {
                binding.etPassword.error = "Silahkan tulis Password Anda"
                binding.etPassword.requestFocus()
            } else {

                val statusUsername = iUsername.indexOf(".")
                if (statusUsername >=0) {
                    binding.etUsername.error = "Silahkan tulis Username Anda tanpa ."
                    binding.etUsername.requestFocus()
                } else {
                    pushLogin(iUsername, iPassword)
                }
            }
        }

        binding.btnDaftar.setOnClickListener {
            val intent = Intent(this@SignInActivity,
                SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()

                } else {
                    if (user.password.equals(iPassword)){
                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("uid", user.uid.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity,
                            HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SignInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}