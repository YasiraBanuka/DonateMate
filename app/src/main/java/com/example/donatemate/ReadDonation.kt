package com.example.donatemate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReadDonation : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var type: TextView
    private lateinit var amount: TextView
    private lateinit var qty: TextView
    private lateinit var desc: TextView

    private var recordId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_donation)

        val userId = FirebaseAuth.getInstance().currentUser?.uid


        name = findViewById(R.id.r_name)
        type = findViewById(R.id.r_type)
        amount = findViewById(R.id.r_amount)
        qty = findViewById(R.id.r_qty)
        desc = findViewById(R.id.r_desc)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Donations").orderByChild("userId").equalTo(userId).limitToLast(1)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lastchild = dataSnapshot.children.lastOrNull() // get the last child node
                if (lastchild != null) {
                    recordId = lastchild.key // store the record ID as a class-level variable
                    val name = lastchild.child("name").value?.toString()
                    val type = lastchild.child("type").value?.toString()
                    val qty = lastchild.child("quantity").value?.toString()
                    val desc = lastchild.child("description").value?.toString()
                    val amount = lastchild.child("amount").value?.toString()

                    name?.let { this@ReadDonation.name.text = it }
                    type?.let { this@ReadDonation.type.text = it }
                    qty?.let { this@ReadDonation.qty.text = it }
                    desc?.let { this@ReadDonation.desc.text = it }
                    amount?.let { this@ReadDonation.amount.text = it }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        val deleteButton: Button = findViewById(R.id.delete_btn)
        deleteButton.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Donations")
            val recordReference = databaseReference.child(recordId ?: "")

            Log.d("DeleteDonation", "Deleting record with ID: $recordId")

            // Remove the record from Firebase
            recordReference.removeValue()

            val intent = Intent(this@ReadDonation, HomePage::class.java)
            startActivity(intent)
        }

        val editButton: Button = findViewById(R.id.edit_btn)
        editButton.setOnClickListener {
            val intent = Intent(this@ReadDonation, EditDonation::class.java)
            intent.putExtra("donationId", recordId)
            startActivity(intent)
        }
    }
}
