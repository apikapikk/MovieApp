package com.rohmanbeny.mov.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rohmanbeny.mov.profile.EditProfileActivity
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.admin.AddMovieActivity
import com.rohmanbeny.mov.sign.signin.SignInActivity
import com.rohmanbeny.mov.utils.Preferences
import com.rohmanbeny.mov.wallet.MyWalletActivity
import com.rohmanbeny.mov.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireContext())

        // Menggunakan binding untuk mengakses view
        binding.tvNama.text = preferences.getValues("nama")
        binding.tvEmail.text = preferences.getValues("email")

        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfile)

        binding.tvMyWallet.setOnClickListener {
            startActivity(Intent(activity, MyWalletActivity::class.java))
        }

        binding.tvEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        binding.tvBahasa.setOnClickListener {
            startActivity(Intent(activity, AddMovieActivity::class.java))
        }

        binding.btnKeluar.setOnClickListener {
            preferences.clear()
            showMessage("Keluar")
            moveIntent()
        }
    }

    private fun moveIntent() {
        startActivity(Intent(activity, SignInActivity::class.java))
        activity?.finish()
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
