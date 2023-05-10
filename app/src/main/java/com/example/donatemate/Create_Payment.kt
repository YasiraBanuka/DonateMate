package com.example.donatemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.donatemate.model.Payment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Create_Payment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_payment)


        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        // Initialize Firebase Database reference
        val databaseRef = FirebaseDatabase.getInstance().getReference("payment")

        // Get references to EditText fields
        val numberEditText = findViewById<EditText>(R.id.cnumber)
        val nameEditText = findViewById<EditText>(R.id.cname)
        val exDateEditText = findViewById<EditText>(R.id.cdate)
        val cvcEditText = findViewById<EditText>(R.id.ccvc)

        // Initialize the RadioGroup
        val paymentMethodGroup: RadioGroup = findViewById(R.id.payment_method_group)

        // Initialize the variables to hold the selected payment method
        var paymentMethod: String? = null

        // Set a listener to the RadioGroup to get the selected RadioButton
        paymentMethodGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_master_card -> {
                    // MasterCard is selected
                    paymentMethod = "Master"
                }
                R.id.radio_visa_card -> {
                    // VisaCard is selected
                    paymentMethod = "Visa"
                }
            }
        }

        // Set an onClickListener for the submit button
        val submitButton = findViewById<Button>(R.id.csubmit)
        submitButton.setOnClickListener {
            // Get the values from the EditTexts
            val accNumber = numberEditText.text.toString().trim()
            val Name = nameEditText.text.toString().trim()
            val ExDate = exDateEditText.text.toString().trim()
            val Cvc = cvcEditText.text.toString().trim()

            // Check if the payment method is selected
            if (paymentMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Check if the card number is valid
            if (accNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a card number", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Check if the CVC is valid
            if (Cvc.isEmpty()) {
                Toast.makeText(this, "Please enter a CVC number", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Create a new Payment object with the values
            val payment = Payment(uid.toString(), paymentMethod!!, accNumber, Name, ExDate, Cvc)

            // Add the Payment object to the Firebase Realtime Database
            databaseRef.push().setValue(payment)
                .addOnSuccessListener {
                    // Show a success message to the user
                    Toast.makeText(this, "Payment added successfully", Toast.LENGTH_LONG).show()

                    // Clear the EditText fields
                    numberEditText.text.clear()
                    nameEditText.text.clear()
                    exDateEditText.text.clear()
                    cvcEditText.text.clear()

                    // Navigate to the read activity
                    val intent = Intent(this, Show_Payment::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    // Show an error message to the user
                    Toast.makeText(this, "Failed to add payment", Toast.LENGTH_LONG).show()
                }
        }
    }
}