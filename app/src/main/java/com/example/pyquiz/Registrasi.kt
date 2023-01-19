package com.example.pyquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registrasi : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        val txt_email = findViewById(R.id.email) as EditText
        val username_regis = findViewById(R.id.username_regis) as EditText
        val password_regis = findViewById(R.id.password_regis) as EditText

        val register = findViewById(R.id.btn_register) as Button
        register.setOnClickListener {
            val email = txt_email.text.toString()
            val pass = password_regis.text.toString()
            val name = username_regis.text.toString()
            val role = "pemain"

            if(email.isEmpty() or pass.isEmpty() or name.isEmpty()){
                Toast.makeText(this, "Isi data yang diperlukan", Toast.LENGTH_SHORT).show()
            } else {
                database = FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user")
                val user_id = database.push().key!!
                val user = user(user_id,email,pass,name,role)
                database.child(user_id).setValue(user).addOnCompleteListener {
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

            val login = findViewById(R.id.login) as TextView
            login.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }