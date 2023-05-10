package com.example.donatemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donatemate.model.MemoriamData
import com.example.donatemate.model.MedicineAdapter
import com.google.firebase.database.*

class SummaryAvailable : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var list: ArrayList<MemoriamData>
    private lateinit var adapter: MedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_memoriam)

        recyclerView = findViewById(R.id.recyclerView)
        databaseReference = FirebaseDatabase.getInstance().getReference("Available Medicines")
        list = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = MedicineAdapter(this, list, databaseReference)
        adapter = MedicineAdapter(this, list) { memoriam ->
            databaseReference.child(memoriam.honourId).removeValue()
        }
        recyclerView.adapter = adapter

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val memoriam = dataSnapshot.getValue(MemoriamData::class.java)
                    list.add(memoriam!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}