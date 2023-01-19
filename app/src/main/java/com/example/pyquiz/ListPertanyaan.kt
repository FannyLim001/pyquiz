package com.example.pyquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListPertanyaan : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var soalRecycleView : RecyclerView
    private lateinit var soalArrayList : ArrayList<soal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pertanyaan)

        val kembali = findViewById<TextView>(R.id.link_kembali)

        kembali.setOnClickListener{
            val intent = Intent(this, AdminBeranda::class.java)
            startActivity(intent)
        }

        val tambah = findViewById<ImageView>(R.id.link_tambah)
        tambah.setOnClickListener{
            val intent = Intent(this, TambahSoal::class.java)
            startActivity(intent)
        }

        soalRecycleView = findViewById(R.id.listPertanyaan)
        soalRecycleView.layoutManager = LinearLayoutManager(this)
        soalRecycleView.setHasFixedSize(true)

        soalArrayList = arrayListOf<soal>()
        getSoalData()
    }

    private fun getSoalData(){
        dbref = FirebaseDatabase.getInstance("https://pyquiz-f602e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("soal")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(soalSnapshot in snapshot.children){
                        val soal = soalSnapshot.getValue(soal::class.java)
                        soalArrayList.add(soal!!)
                    }
                    soalRecycleView.adapter = MyAdapter(ArrayList(soalArrayList.distinct()),object : MyAdapter.OptionsMenuClickListener{
                        override fun onOptionsMenuClicked(position: Int) {
                            performOptionsMenuClick(position)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun performOptionsMenuClick(position: Int) {
        val popupMenu = PopupMenu(this , soalRecycleView[position].findViewById(R.id.txt_option))
        // add the menu
        popupMenu.inflate(R.menu.option_menu)
        // implement on menu item click Listener
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.menu_edit -> {
                        // here are the logic to delete an item from the list
                        val intent = Intent(this@ListPertanyaan, EditSoal::class.java)
                        intent.putExtra("id_soal",soalArrayList[position].soal_id)
                        startActivity(intent)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

}