package com.example.donatemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.donatemate.model.Donation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddDonation : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donation)

        // Get a reference to your Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference("Donations")
        auth = FirebaseAuth.getInstance()

        // Get references to the EditText views
        val nameEditText = findViewById<EditText>(R.id.dName)
        val typeEditText = findViewById<EditText>(R.id.dType)
        val qtyTypeEditText = findViewById<EditText>(R.id.dqty)
        val amountEditText = findViewById<EditText>(R.id.damount)
        val discEditText = findViewById<EditText>(R.id.ddiscr)

        // Set an onClickListener for the submit button
        val submitButton = findViewById<Button>(R.id.dsubmit)
        submitButton.setOnClickListener {
            // Get the values from the EditTexts
            val name = nameEditText.text.toString().trim()
            val type = typeEditText.text.toString().trim()
            val qty = qtyTypeEditText.text.toString().trim()
            val dics = discEditText.text.toString().trim()
            val amount = amountEditText.text.toString().trim()

            // Check if the amount is valid
            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Get the current user ID
            val userId = auth.currentUser?.uid

            if (userId != null) {
                // Create a new Donation object with the values
                val donation = Donation(userId, name, type, qty, dics, amount)

                // Add the Donation object to the Firebase Realtime Database
                databaseRef.push().setValue(donation)
                    .addOnSuccessListener {
                        // Show a success message to the user
                        Toast.makeText(this, "Donation added successfully", Toast.LENGTH_LONG).show()

                        // Clear the EditText fields
                        nameEditText.text.clear()
                        typeEditText.text.clear()
                        discEditText.text.clear()
                        amountEditText.text.clear()
                        qtyTypeEditText.text.clear()

                        // Navigate to the read activity
                        val intent = Intent(this, ReadDonation::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // Show an error message to the user
                        Toast.makeText(this, "Failed to add donation", Toast.LENGTH_LONG).show()
                    }
            } else {
                // Show an error message to the user
                Toast.makeText(this, "Failed to get current user ID", Toast.LENGTH_LONG).show()
            }
        }
    }
}
