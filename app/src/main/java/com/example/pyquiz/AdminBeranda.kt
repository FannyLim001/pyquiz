package com.example.pyquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.pyquiz.databinding.ActivityAdminBerandaBinding
import com.example.pyquiz.fragments.*

class AdminBeranda : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBerandaBinding
    private val userVM: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AdminHomeFragment())
        binding.adminNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.admin_home -> replaceFragment(AdminHomeFragment())
                R.id.admin_kuis -> replaceFragment(AdminQuizFragment())
                R.id.admin_profile -> replaceFragment(AdminProfileFragment())

                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.admin_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}