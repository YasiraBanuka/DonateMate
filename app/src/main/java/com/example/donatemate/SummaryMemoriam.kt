package com.example.donatemate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SummaryMemoriam : AppCompatActivity() {

    private lateinit var Name: TextView
    private lateinit var Relationship: TextView
    private lateinit var Bio: TextView
    private var recordId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_memoriam)

        Name = findViewById(R.id.r_hName)
        Relationship = findViewById(R.id.r_hrelationship)
        Bio = findViewById(R.id.r_hbio)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Memoriam").limitToLast(1)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lastchild = dataSnapshot.children.last() // get the first child node
                recordId = lastchild.key // store the record ID as a class-level variable
                val name = lastchild.child("honourname").value?.toString()
                val relation = lastchild.child("honourrelationship").value?.toString()
                val bio = lastchild.child("honourbio").value?.toString()

                Name.text = name
                Relationship.text = relation
                Bio.text = bio
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


        val deleteButton: Button = findViewById(R.id.deletebtn)
        deleteButton.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Memoriam")
            val recordReference = databaseReference.child(recordId ?: "")

            Log.d("DeleteIncome", "Deleting record with ID: $recordId")

            // Remove the record from Firebase
            recordReference.removeValue()

            val intent = Intent(this@SummaryMemoriam, HomePage::class.java)

        }


        val editButton: Button = findViewById(R.id.uedit_btn)
        editButton.setOnClickListener {
            val intent = Intent(this@SummaryMemoriam, UpdateMemoriam::class.java)
            startActivity(intent)
        }
    }
}