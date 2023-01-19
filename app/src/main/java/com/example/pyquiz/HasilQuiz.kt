package com.example.pyquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HasilQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_quiz)

        val profileName=intent.getStringExtra("Username")
        val sharedPreferences = getSharedPreferences("skor", MODE_PRIVATE)
        val skor = sharedPreferences.getString("value", "")
        val username = findViewById<TextView>(R.id.hasil_username)
        username.text = profileName
        val hasil_skor = findViewById<TextView>(R.id.hasil_skor)
        hasil_skor.text = "Skor kamu adalah "+skor

        val selesai = findViewById<Button>(R.id.btn_selesai)
        selesai.setOnClickListener{
            val intent = Intent(this, Beranda::class.java)
            intent.putExtra("Username",profileName)
            startActivity(intent)
        }
    }
}