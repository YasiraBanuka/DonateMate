package com.example.donatemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.donatemate.databinding.ActivityAddMemoriamBinding
import com.example.donatemate.model.MemoriamData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddMemoriam : AppCompatActivity() {

    private lateinit var binding: ActivityAddMemoriamBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemoriamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            val name = binding.hName.text.toString().trim()
            val relationship = binding.hRelationship.text.toString().trim()
            val bio = binding.hBio.text.toString().trim()

            // Check if any input field is empty
            if (name.isEmpty() || relationship.isEmpty() || bio.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            databaseReference = FirebaseDatabase.getInstance().getReference("Memoriam")
            val id = databaseReference.push().key // generate a unique id for the medicine
            val memo = MemoriamData(id!!, name, relationship, bio) // create a new medicine with the generated id

            // add the new medicine to the database
            databaseReference.child(id).setValue(memo).addOnSuccessListener {
                binding.hName.text?.clear()
                binding.hRelationship.text?.clear()
                binding.hBio.text?.clear()

                Toast.makeText(this, "Data inserted Successfully", Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
            }
        }
    }
}