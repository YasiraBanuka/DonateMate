package com.example.donatemate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.donatemate.databinding.ActivityEditDonationBinding
import com.example.donatemate.model.Donation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class EditDonation : AppCompatActivity() {

    private lateinit var binding: ActivityEditDonationBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private var recordId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_donation)

        // Initialize the Firebase database and current user ID
        database = FirebaseDatabase.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Query the database for the last donation record entered by the current user
        database.getReference("Donations")
            .orderByChild("userId")
            .equalTo(userId)
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val donation = dataSnapshot.children.firstOrNull()?.getValue(Donation::class.java)

                        // Set the values of the EditText fields
                        binding.eName.setText(donation?.name)
                        binding.eType.setText(donation?.type)
                        binding.eamount.setText(donation?.amount)
                        binding.eqty.setText(donation?.quantity)
                        binding.ediscr.setText(donation?.description)

                        // Set up the submit button onClick listener
                        binding.esubmit.setOnClickListener {
                            val nameValue = binding.eName.text.toString()
                            val typeValue = binding.eType.text.toString()
                            val amountValue = binding.eamount.text.toString()
                            val quantityValue = binding.eqty.text.toString()
                            val descriptionValue = binding.ediscr.text.toString()

                            // Update the donation record in the database
                            donation?.let {
                                val donationId =  UUID.randomUUID().toString()
                                val updatedDonation = Donation(
                                    it.userId,
                                    nameValue,
                                    typeValue,
                                    amountValue,
                                    quantityValue,
                                    descriptionValue

                                )

                                database.getReference("Donations").child(donationId).setValue(updatedDonation)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@EditDonation,
                                            "Record updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            this@EditDonation,
                                            "Failed to update record",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@EditDonation,
                            "Donation record not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EditDonation,
                        "Failed to load record",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            })
    }

}
