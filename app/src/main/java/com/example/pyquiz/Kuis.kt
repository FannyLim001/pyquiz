package com.example.pyquiz

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class Kuis : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var no : TextView
    lateinit var gmbr : ImageView
    lateinit var btn_ops1 : Button
    lateinit var btn_ops2 : Button
    lateinit var btn_ops3 : Button
    var no_soal: Int = 0
    var skor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuis)

        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val username = sharedPreferences.getString("value", "")

        var getUsername: String = ""

        database =
            FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("user")
        database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    getUsername = user.child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        no = findViewById(R.id.nomor_soal)
        gmbr = findViewById(R.id.gambar_soal)
        btn_ops1 = findViewById(R.id.btn_option1)
        btn_ops2 = findViewById(R.id.btn_option2)
        btn_ops3 = findViewById(R.id.btn_option3)
        var gmbr_nama : String = ""
        var btn1 : String = ""
        var btn2 : String = ""
        var btn3 : String = ""
        var jwbn : String = ""

        database =
            FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("soal")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (soal in snapshot.children) {
                    gmbr_nama = soal.child("gambar_soal").value.toString()
                    btn1 = soal.child("opsi1").value.toString()
                    btn2 = soal.child("opsi2").value.toString()
                    btn3 = soal.child("opsi3").value.toString()
                    jwbn = soal.child("jawaban").value.toString()
                    //Log.i(TAG,gmbr_nama)

                    val pictStorage =
                        FirebaseStorage.getInstance("gs://pyquiz-f602e.appspot.com").reference.child(
                            "gambar_soal/$gmbr_nama"
                        )
                    val localfile = File.createTempFile("tempImage", "jpg")
                    pictStorage.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        gmbr.setImageBitmap(bitmap)
                    }.addOnFailureListener {
                        Toast.makeText(this@Kuis, "Gambar gagal diupload", Toast.LENGTH_SHORT)
                            .show()
                    }

                    btn_ops1.text = btn1
                    btn_ops2.text = btn2
                    btn_ops3.text = btn3

                    btn_ops1.setOnClickListener{

                        if(btn_ops1.text == jwbn){
                            skor++
                        } else {
                            Toast.makeText(this@Kuis, "Jawaban Salah", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btn_ops2.setOnClickListener{

                        if(btn_ops2.text == jwbn){
                            skor++
                        } else {
                            Toast.makeText(this@Kuis, "Jawaban Salah", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btn_ops3.setOnClickListener{

                        if(btn_ops3.text == jwbn){
                            skor++
                        } else {
                            Toast.makeText(this@Kuis, "Jawaban Salah", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val submit = findViewById<Button>(R.id.btn_submit)
        submit.setOnClickListener {
            val sharedPref = getSharedPreferences("skor", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("value", skor.toString())
            editor.apply()
            val intent = Intent(this, HasilQuiz::class.java)
            intent.putExtra("Username", getUsername)
            startActivity(intent)
        }

        val kembali = findViewById<ImageView>(R.id.btn_kembali)
        kembali.setOnClickListener {
            val intent = Intent(this, Beranda::class.java)
            intent.putExtra("Username", getUsername)
            startActivity(intent)
        }
    }

}