package com.example.donatemate

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

    //donation
        val button1: LinearLayout = findViewById(R.id.askdonat)
        button1.setOnClickListener {
            val intent = Intent(this@HomePage, AddDonation::class.java)
            startActivity(intent)
        }

        //payment money
        val button2: LinearLayout = findViewById(R.id.payment)
        button2.setOnClickListener {
            val intent = Intent(this@HomePage, Create_Payment::class.java)
            startActivity(intent)
        }

        //memori
        val button3: LinearLayout = findViewById(R.id.memoria)
        button3.setOnClickListener {
            val intent = Intent(this@HomePage, MainActivity::class.java)
            startActivity(intent)
        }

        //profile
        val button4: LinearLayout = findViewById(R.id.profil)
        button4.setOnClickListener {
            val intent = Intent(this@HomePage, Userprofile::class.java)
            startActivity(intent)
        }
    }
}
