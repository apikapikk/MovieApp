package com.rohmanbeny.mov.sign.signup

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivitySignUpPhotoscreenBinding
import com.rohmanbeny.mov.home.HomeActivity
import com.rohmanbeny.mov.sign.signin.User
import com.rohmanbeny.mov.utils.Preferences
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences
    private lateinit var binding: ActivitySignUpPhotoscreenBinding
    lateinit var user: User
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPhotoscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        this.intent.getSerializableExtra("data")?.let { user = it as User }
        binding.tvHello.text = "Selamat Datang\n${user.nama}"

        binding.ivAdd.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                binding.btnSave.visibility = View.INVISIBLE
                binding.ivAdd.setImageResource(R.drawable.ic_btn_upload)
                binding.ivProfile.setImageResource(R.drawable.user_pic)
            } else {
                ImagePicker.with(this)
                    .cameraOnly()    //User can only capture image using Camera
                    .start()
            }
        }

        binding.btnHome.setOnClickListener {
            finishAffinity()
            val goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
            startActivity(goHome)
        }
        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                            saveToFirebase(it.toString()) // Save the URL to Firebase
                        }
                        val goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload ${progress.toInt()}%")
                    }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToFirebase(url: String) {
        // Update the user object with the new URL
        val updatedUser = user.copy(url = url)

        mFirebaseDatabase.child(user.username!!).setValue(updatedUser)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    preferences.setValues("nama", updatedUser.nama)
                    preferences.setValues("user", updatedUser.username)
                    preferences.setValues("saldo", updatedUser.saldo)
                    preferences.setValues("url", updatedUser.url)
                    preferences.setValues("email", updatedUser.email)
                    preferences.setValues("status", "1")

                    finishAffinity()
                    val intent = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpPhotoScreenActivity, "Failed to save data", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@SignUpPhotoScreenActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .cameraOnly()    // User can only capture image using Camera
            .start()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak dapat menambahkan photo Profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Tergesah? klik tombol upload nanti aja", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        // Handle rationale if needed
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfile)

            binding.btnSave.visibility = View.VISIBLE
            binding.ivAdd.setImageResource(R.drawable.ic_btn_delete)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
