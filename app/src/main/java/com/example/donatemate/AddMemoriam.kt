package com.example.donatemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.donatemate.model.Memoriam
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddMemoriam : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memoriam)

        // Get a reference to your Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference("Memoriam")
        auth = FirebaseAuth.getInstance()

        // Get references to the EditText views
        val nameEditText = findViewById<EditText>(R.id.hName)
        val relationshipEditText = findViewById<EditText>(R.id.hRelationship)
        val bioEditText = findViewById<EditText>(R.id.hBio)

        // Set an onClickListener for the submit button
        val submitButton = findViewById<Button>(R.id.submitBtn)
        submitButton.setOnClickListener {
            // Get the values from the EditTexts
            val name = nameEditText.text.toString().trim()
            val relationship = relationshipEditText.text.toString().trim()
            val bio = bioEditText.text.toString().trim()

            val userId = getCurrentUserId()

            // Create a new Memoriam object with the values
            val memoriam = Memoriam(userId, name, relationship, bio)

            // Add the Memoriam object to the Firebase Realtime Database
            databaseRef.push().setValue(memoriam)
                .addOnSuccessListener {
                    // Show a success message to the user
                    Toast.makeText(this, "Memoriam added successfully", Toast.LENGTH_LONG).show()

                    // Clear the EditText fields
                    nameEditText.text.clear()
                    relationshipEditText.text.clear()
                    bioEditText.text.clear()

                    // Navigate to the summary activity
                    val intent = Intent(this, SummaryMemoriam::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    // Show an error message to the user
                    Toast.makeText(this, "Failed to add memoriam", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun getCurrentUserId(): String {
        val currentUser = auth.currentUser
        return currentUser?.uid ?: ""
    }
}
