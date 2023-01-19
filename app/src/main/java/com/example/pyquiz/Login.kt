package com.example.pyquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class Login : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val name = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val login = findViewById<Button>(R.id.btn_login)
        login.setOnClickListener{
            val username = name.text.toString()
            val pass = password.text.toString()

            if(username.isEmpty()){
                Toast.makeText(this, "Email harus diisi", Toast.LENGTH_SHORT).show()
            }

            if(pass.isEmpty()){
                Toast.makeText(this, "Password harus diisi", Toast.LENGTH_SHORT).show()
            }

            if(username.equals("admin") and pass.equals("admin12")){
                Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Login, AdminBeranda::class.java)
                startActivity(intent)
            } else if (!username.equals("admin") or !pass.equals("admin12")) {
                Toast.makeText(this@Login, "Login Gagal", Toast.LENGTH_SHORT).show()
            }

            database = FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("user")
            database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(user in snapshot.children){
                        val getUsername = user.child("username").value.toString()
                        val getPass = user.child("password").value.toString()
                        val getRole = user.child("role").value.toString()

                        if(getRole.equals("pemain")){
                            if((username == getUsername) and (pass == getPass)){
                                Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
                                val sharedPref = getSharedPreferences("myKey", MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("value", getUsername)
                                editor.apply()
                                val intent = Intent(this@Login, Beranda::class.java)
                                intent.putExtra("Username",getUsername)
                                startActivity(intent)
                            } else if ((username != getUsername) or (pass != getPass)) {
                                Toast.makeText(this@Login, "Login Gagal", Toast.LENGTH_SHORT).show()
                            }
                        }

                        // Log.i(TAG,getPass)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        val register = findViewById(R.id.register) as TextView
        register.setOnClickListener{
            val intent = Intent(this, Registrasi::class.java)
            startActivity(intent)
        }
    }
}