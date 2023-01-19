package com.example.pyquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pyquiz.databinding.ActivityBerandaBinding
import com.example.pyquiz.fragments.HomeFragment
import com.example.pyquiz.fragments.ProfileFragment
import com.example.pyquiz.fragments.QuizFragment

class Beranda : AppCompatActivity() {
    private lateinit var binding: ActivityBerandaBinding
    private val userVM : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.nav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.kuis -> replaceFragment(QuizFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {
                }
            }
            true
        }
        val profileName=intent.getStringExtra("Username")
        if (profileName != null) {
            userVM.setData(profileName)
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}