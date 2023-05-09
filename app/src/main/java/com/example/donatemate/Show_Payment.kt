package com.example.donatemate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Show_Payment : AppCompatActivity() {

    //    private lateinit var cardTyp: TextView
    private lateinit var accNu: TextView
    private lateinit var accname: TextView
    private lateinit var exDat: TextView
    private lateinit var cvcN: TextView
    private var recordId: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_payment)


        // Get references to EditText fields
        accNu = findViewById<EditText>(R.id.cardNoLabel)
        accname = findViewById<EditText>(R.id.cardNameLabel)
        exDat = findViewById<EditText>(R.id.exDatelbl)
        cvcN = findViewById<EditText>(R.id.cvcLabel)


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("payment").limitToLast(1)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val lastchild = dataSnapshot.children.last() // get the first child node

                recordId = lastchild.key // store the record ID as a class-level variable
                val accNum = lastchild.child("cardnumber").value?.toString()
                val accName = lastchild.child("name").value?.toString()
                val ExDate = lastchild.child("exDate").value?.toString()
                val CVC = lastchild.child("cvcNo").value?.toString()


                accNu.text = accNum
                accname.text = accName
                exDat.text = ExDate
                cvcN.text = CVC
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        val deleteButton: Button = findViewById(R.id.buttonCancel)
        deleteButton.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("payment")
            val recordReference = databaseReference.child(recordId ?: "")

            Log.d("DeleteIncome", "Deleting record with ID: $recordId")

            // Remove the record from Firebase
            recordReference.removeValue()
                .addOnSuccessListener {
                    Log.d("DeleteIncome", "Record deleted successfully")
                    val intent = Intent(this@Show_Payment, HomePage::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w("DeleteIncome", "Error deleting record", e)
                    // Show an error message to the user
                    Toast.makeText(this@Show_Payment, "Error deleting record: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }


        val editButton: Button = findViewById(R.id.updateButton)
        editButton.setOnClickListener {
            val intent = Intent(this@Show_Payment, Edit_Payment::class.java)
            intent.putExtra("recordId", recordId)
            startActivity(intent)
        }


    }
}