package com.example.donatemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateMemoriam : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var relationship: TextView
    private lateinit var bio: TextView
    private var recordId: String? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_memoriam)

        // Initialize the TextViews
        name = findViewById(R.id.uhName)
        relationship = findViewById(R.id.uhRelationship)
        bio = findViewById(R.id.uhBio)

        // Initialize the Buttons
        val submitButton = findViewById<Button>(R.id.esubmitbtn)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance().reference

        // Query the database to retrieve the last row of data
        database.child("Memoriam").orderByKey().limitToLast(1).get()
            .addOnSuccessListener { dataSnapshot ->
                // Update the values of that row with the new data
                // Get the first child node
                val lastChild = dataSnapshot.children.last()
                // Store the record ID as a class-level variable
                recordId = lastChild.key
                // Get the values of the child nodes and convert them to strings
                val nam = lastChild.child("honourname").value?.toString()
                val rel = lastChild.child("honourrelationship").value?.toString()
                val bi = lastChild.child("honourbio").value?.toString()

                // Set the values of the TextViews
                name.text = nam
                relationship.text = rel
                bio.text = bi

                // Set up the submit button onClick listener
                submitButton.setOnClickListener {
                    // Get the updated input values
                    val uNam = name.text.toString()
                    val uRel = relationship.text.toString()
                    val uBio = bio.text.toString()

                    // Update the  record with the new values
                    val userId = getCurrentUserId()

                    lastChild.ref.updateChildren(
                        mapOf(
                            "userId"  to userId,
                            "honourname" to uNam,
                            "category" to uRel,
                            "amount" to uBio
                        )
                    )
                    // Show a toast message indicating that the record was updated
                    Toast.makeText(this@UpdateMemoriam, "Record updated successfully", Toast.LENGTH_SHORT).show()
                    // Finish the activity and return to the previous screen
                    finish()
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = auth.currentUser
        return currentUser?.uid ?: ""
    }
}
