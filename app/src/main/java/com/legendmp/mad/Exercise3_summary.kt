package com.legendmp.mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Exercise3_summary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise3_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize UI elements
        val tvCardType: TextView = findViewById(R.id.tvCardType)
        val tvCardNumber: TextView = findViewById(R.id.tvCardNumber)
        val tvExpiryDate: TextView = findViewById(R.id.tvExpiryDate)
        val tvSecurityCode: TextView = findViewById(R.id.tvSecurityCode)
        val tvAddress: TextView = findViewById(R.id.tvAddress)
        val tvCity: TextView = findViewById(R.id.tvCity)
        val tvState: TextView = findViewById(R.id.tvState)
        val tvZipCode: TextView = findViewById(R.id.tvZipCode)
        val tvCountry: TextView = findViewById(R.id.tvCountry)
        val btnBack: Button = findViewById(R.id.btnBack)

        // Retrieve data from intent
        val intent = intent
        tvCardType.text = intent.getStringExtra("cardType")
        tvCardNumber.text = maskCardNumber(intent.getStringExtra("cardNumber") ?: "")
        tvExpiryDate.text = intent.getStringExtra("expiryDate")
        tvSecurityCode.text = "***" // Hide security code for safety
        tvAddress.text = intent.getStringExtra("address")
        tvCity.text = intent.getStringExtra("city")
        tvState.text = intent.getStringExtra("state")
        tvZipCode.text = intent.getStringExtra("zipCode")
        tvCountry.text = intent.getStringExtra("country")

        // Back button to return to previous screen
        btnBack.setOnClickListener {
            val backIntent = Intent(this, Exercise3::class.java)
            startActivity(backIntent)
            finish()
        }
    }
    private fun maskCardNumber(cardNumber: String): String {
        return if (cardNumber.length == 16) {
            "**** **** **** " + cardNumber.takeLast(4)
        } else {
            "Invalid Card Number"
        }
    }
}