package com.example.donatemate

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.donatemate.databinding.ActivityEditDonationBinding
import com.example.donatemate.model.Donation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.temporal.TemporalAmount
import java.util.*

class EditDonation : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var name: TextView
    private lateinit var type: TextView
    private lateinit var amount: TextView
    private lateinit var qty: TextView
    private lateinit var decs: TextView


    private var recordId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_donation)

        // Initialize the Firebase database and current user ID
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""


        // Initialize the TextViews
        name = findViewById(R.id.eName)
        type = findViewById(R.id.eType)
        qty = findViewById(R.id.eqty)
        amount = findViewById(R.id.eamount)
        decs = findViewById(R.id.ediscr)


        // Initialize the Buttons
        var submitButton = findViewById<Button>(R.id.esubmit)

        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance().reference
        // Query the database to retrieve the last row of data
        database.child("Donations").orderByKey().limitToLast(1).get()
            .addOnSuccessListener { dataSnapshot ->
                // Store the record ID as a class-level variable
                val lastChild = dataSnapshot.children.last()
                // Store the record ID as a class-level variable
                recordId =
                    lastChild.key                        // Set the values of the EditText fields
                // Get the values of the child nodes and convert them to strings
                val Name = lastChild.child("name").value?.toString()
                val Type = lastChild.child("type").value?.toString()
                val Quantity = lastChild.child("quantity").value?.toString()
                val Description = lastChild.child("description").value?.toString()
                val Amount = lastChild.child("amount").value?.toString()

                // Set the values of the TextViews
                name.text = Name
                type.text = Type
                qty.text = Quantity
                decs.text = Description
                amount.text = Amount


                // Set up the submit button onClick listener
                submitButton.setOnClickListener {
                    // Get the updated input values
                    // Get the updated input values
                    val nam = name.text.toString()
                    val typ = type.text.toString()
                    val Qty = qty.text.toString()
                    val amoun = amount.text.toString()
                    val dec = decs.text.toString()


                    // Update the income record with the new values
                    lastChild.ref.updateChildren(
                        mapOf(
                            "amount" to amoun,
                            "name" to nam,
                            "type" to typ,
                            "description" to dec,
                            "quantity" to Qty
                        )
                    )
                    // Show a toast message indicating that the record was updated
                    Toast.makeText(
                        this@EditDonation,
                        "Record updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Finish the activity and return to the previous screen
                    finish()
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }
    }


}
