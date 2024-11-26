package com.rohmanbeny.mov.home

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rohmanbeny.mov.R
import com.rohmanbeny.mov.databinding.ActivityHomeBinding
import com.rohmanbeny.mov.home.dashboard.DashboardFragment
import com.rohmanbeny.mov.setting.SettingFragment
import com.rohmanbeny.mov.tiket.TicketFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentHome = DashboardFragment()
        val fragmentTicket = TicketFragment()
        val fragmentSetting = SettingFragment()

        setFragment(fragmentHome)

        binding.ivMenu1.setOnClickListener{
            setFragment(fragmentHome)

            changeIcon(binding.ivMenu1, R.drawable.ic_home_active)
            changeIcon(binding.ivMenu2, R.drawable.ic_tiket)
            changeIcon(binding.ivMenu3, R.drawable.ic_profile)
        }
        binding.ivMenu2.setOnClickListener{
            setFragment(fragmentTicket)

            changeIcon(binding.ivMenu1, R.drawable.ic_home)
            changeIcon(binding.ivMenu2, R.drawable.ic_tiket_active)
            changeIcon(binding.ivMenu3, R.drawable.ic_profile)
        }
        binding.ivMenu3.setOnClickListener{
            setFragment(fragmentSetting)

            changeIcon(binding.ivMenu1, R.drawable.ic_home)
            changeIcon(binding.ivMenu2, R.drawable.ic_tiket)
            changeIcon(binding.ivMenu3, R.drawable.ic_profile_active)
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeIcon(imageView: ImageView, int: Int) {
        imageView.setImageResource(int)
    }
}