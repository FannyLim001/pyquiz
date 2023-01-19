package com.example.pyquiz

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class TambahSoal : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    var selectedPict : String = ""
    lateinit var pictUrl : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_soal)

        val gambar_soal = findViewById(R.id.upload_soal) as EditText
        val txt_ops1 = findViewById(R.id.tambah_opsi1) as EditText
        val txt_ops2 = findViewById(R.id.tambah_opsi2) as EditText
        val txt_ops3 = findViewById(R.id.tambah_opsi3) as EditText
        val txt_jwbn = findViewById(R.id.tambah_jawaban) as EditText
        val tambah = findViewById(R.id.btn_tambah_soal) as Button

        gambar_soal.setOnClickListener{
            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 111)
            gambar_soal.setText(selectedPict)
        }

        tambah.setOnClickListener{
            val gmbr = gambar_soal.text.toString()
            val ops1 = txt_ops1.text.toString()
            val ops2 = txt_ops2.text.toString()
            val ops3 = txt_ops3.text.toString()
            val jwbn = txt_jwbn.text.toString()

            database = FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("soal")
            val soal_id = database.push().key!!
            val soal = soal(soal_id,gmbr,ops1,ops2,ops3,jwbn)
            database.child(soal_id).setValue(soal).addOnCompleteListener {
                Toast.makeText(this, "Tambah Soal Berhasil", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Tambah Soal Gagal", Toast.LENGTH_SHORT).show()
            }

            val pictStorage = FirebaseStorage.getInstance("gs://pyquiz-f602e.appspot.com").getReference("gambar_soal/$gmbr")
            pictStorage.putFile(pictUrl).addOnSuccessListener {
                Toast.makeText(this, "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ListPertanyaan::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            pictUrl = data?.data!!
            val urlString = pictUrl.toString()
            val removedString = urlString.substring(urlString.lastIndexOf("%")+1)
            selectedPict = removedString.replace("2F","")

        }
    }

}