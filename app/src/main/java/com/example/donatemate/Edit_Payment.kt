package com.example.donatemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Edit_Payment : AppCompatActivity() {

    private lateinit var cardTyp: TextView
    private lateinit var accNu: TextView
    private lateinit var accname: TextView
    private lateinit var exDat: TextView
    private lateinit var cvcN: TextView
    private var recordId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_payment)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        // Get references to EditText fields
        // Initialize the RadioGroup
        val paymentMethodGroup: RadioGroup = findViewById(R.id.epayment_method_group)

        // Initialize the variables to hold the selected payment method
        var paymentMethod: String? = null

        // Set a listener to the RadioGroup to get the selected RadioButton
        paymentMethodGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.eradio_master_card -> {
                    // MasterCard is selected
                    paymentMethod = "Master"
                }
                R.id.eradio_visa_card -> {
                    // VisaCard is selected
                    paymentMethod = "Visa"
                }
            }
        }
        accNu = findViewById<EditText>(R.id.enumber)
        accname = findViewById<EditText>(R.id.ename)
        exDat = findViewById<EditText>(R.id.edate)
        cvcN = findViewById<EditText>(R.id.ecvc)

        // Initialize the Buttons
        var submitButton = findViewById<Button>(R.id.esubmit)

        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance().reference
        // Query the database to retrieve the last payment record of the current user
        database.child("payment").orderByChild("userID").equalTo(uid).limitToLast(1).get()
            .addOnSuccessListener { dataSnapshot ->

                val lastchild = dataSnapshot.children.last() // get the first child node

                recordId = lastchild.key // store the record ID as a class-level variable
                val accType = lastchild.child("type").value?.toString()
                val accNum = lastchild.child("cardnumber").value?.toString()
                val accName = lastchild.child("name").value?.toString()
                val ExDate = lastchild.child("exDate").value?.toString()
                val CVC = lastchild.child("cvcNo").value?.toString()

                // Set the values of the TextViews
                paymentMethod = accType
                accNu.text = accNum
                accname.text = accName
                exDat.text = ExDate
                cvcN.text = CVC

                // Set up the submit button onClick listener
                submitButton.setOnClickListener {
                    // Get the updated input values
                    // Get the updated input values
                    val accountType = paymentMethod.toString()
                    val accNumber = accNu.text.toString()
                    val accName = accname.text.toString()
                    val Exdate = exDat.text.toString()
                    val CVC = cvcN.text.toString()


                    // Update the income record with the new values
                    lastchild.ref.updateChildren(
                        mapOf(
                            "userID" to uid,
                            "type" to accountType,
                            "cardnumber" to accNumber,
                            "name" to accName,
                            "exDate" to Exdate,
                            "cvcNo" to CVC
                        )
                    )
                    // Show a toast message indicating that the record was updated
                    Toast.makeText(this@Edit_Payment, "Record updated successfully", Toast.LENGTH_SHORT).show()
                    // Finish the activity and return to the previous screen
                    finish()
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }


    }
}