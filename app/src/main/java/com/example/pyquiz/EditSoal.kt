package com.example.pyquiz

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class EditSoal : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var selectedPict : String = ""
    lateinit var pictUrl : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_soal)

        val id_soal = intent.getStringExtra("id_soal")

        val gambar_soal = findViewById(R.id.update_gambar) as EditText
        val txt_ops1 = findViewById(R.id.update_opsi1) as EditText
        val txt_ops2 = findViewById(R.id.update_opsi2) as EditText
        val txt_ops3 = findViewById(R.id.update_opsi3) as EditText
        val txt_jwbn = findViewById(R.id.update_jawaban) as EditText
        val update = findViewById(R.id.btn_update_soal) as Button
        val delete = findViewById(R.id.btn_delete_soal) as Button

        gambar_soal.setOnClickListener{
            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 111)
            gambar_soal.setText(selectedPict)
        }

        database = FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("soal")
        if (id_soal != null) {
            database.orderByChild("soal_id").equalTo(id_soal).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(soal in snapshot.children){
                        val getOpsi1 = soal.child("opsi1").value.toString()
                        val getOpsi2 = soal.child("opsi2").value.toString()
                        val getOpsi3 = soal.child("opsi3").value.toString()
                        val getJawaban = soal.child("jawaban").value.toString()

                        txt_ops1.setText(getOpsi1)
                        txt_ops2.setText(getOpsi2)
                        txt_ops3.setText(getOpsi3)
                        txt_jwbn.setText(getJawaban)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        update.setOnClickListener{
            val gmbr = gambar_soal.text.toString()
            val ops1 = txt_ops1.text.toString()
            val ops2 = txt_ops2.text.toString()
            val ops3 = txt_ops3.text.toString()
            val jwbn = txt_jwbn.text.toString()

            if(id_soal != null){
                val soal = soal(id_soal,gmbr,ops1,ops2,ops3,jwbn)
                database.child(id_soal).setValue(soal).addOnCompleteListener {
                    Toast.makeText(this, "Edit Soal Berhasil", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Edit Soal Gagal", Toast.LENGTH_SHORT).show()
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

        delete.setOnClickListener{
            if(id_soal != null) {
                database.child(id_soal).removeValue().addOnCompleteListener {
                    Toast.makeText(this, "Hapus Soal Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ListPertanyaan::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Hapus Soal Gagal", Toast.LENGTH_SHORT).show()
                }
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