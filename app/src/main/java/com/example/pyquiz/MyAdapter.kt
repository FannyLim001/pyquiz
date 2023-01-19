package com.example.pyquiz

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MyAdapter(private val soalList : ArrayList<soal>, private val optionsMenuClickListener : OptionsMenuClickListener): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var count = 1
    private lateinit var database: DatabaseReference

    interface OptionsMenuClickListener{
        fun onOptionsMenuClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pertanyaan_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = soalList[position]
        val no = count++
        val pictStorage =
            FirebaseStorage.getInstance("gs://pyquiz-f602e.appspot.com").reference.child(
                "gambar_soal/${currentitem.gambar_soal}"
            )
        val localfile = File.createTempFile("tempImage", "jpg")
        pictStorage.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.gambar_pertanyaan.setImageBitmap(bitmap)
        }
        holder.no_pertanyaan.text = "Pertanyaan " + no
        holder.jawaban.text = "Jawaban: " + currentitem.jawaban
        holder.btn_opsi.setOnClickListener{
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return soalList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val gambar_pertanyaan : ImageView = itemView.findViewById(R.id.tampilan_gambar)
        val btn_opsi : TextView = itemView.findViewById(R.id.txt_option)
        val no_pertanyaan : TextView = itemView.findViewById(R.id.no_pertanyaan)
        val jawaban : TextView = itemView.findViewById(R.id.jawaban_pertanyaan)
    }
}