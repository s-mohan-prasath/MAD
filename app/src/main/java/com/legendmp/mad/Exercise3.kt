package com.legendmp.mad

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Toast

class Exercise3 : AppCompatActivity() {
    private lateinit var spinnerCardType: Spinner
    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etSecurityCode: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var spinnerState: Spinner
    private lateinit var etZipCode: EditText
    private lateinit var spinnerCountry: Spinner
    private lateinit var btnSubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerCardType = findViewById(R.id.spinnerCardType)
        etCardNumber = findViewById(R.id.etCardNumber)
        etExpiryDate = findViewById(R.id.etExpiryDate)
        etSecurityCode = findViewById(R.id.etSecurityCode)
        etAddress = findViewById(R.id.etAddress)
        etCity = findViewById(R.id.etCity)
        spinnerState = findViewById(R.id.spinnerState)
        etZipCode = findViewById(R.id.etZipCode)
        spinnerCountry = findViewById(R.id.spinnerCountry)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Populate Spinners
        val cardTypes = arrayOf("Select Card Type", "Visa", "MasterCard", "American Express")
        val states = arrayOf("Select State", "California", "Texas", "New York")
        val countries = arrayOf("Select Country", "USA", "Canada", "UK")

        spinnerCardType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cardTypes)
        spinnerState.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, states)
        spinnerCountry.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)

        // Submit button click event
        btnSubmit.setOnClickListener {
            if (validateFields()) {
                val intent = Intent(this, Exercise3_summary::class.java)
                intent.putExtra("cardType", spinnerCardType.selectedItem.toString())
                intent.putExtra("cardNumber", etCardNumber.text.toString())
                intent.putExtra("expiryDate", etExpiryDate.text.toString())
                intent.putExtra("securityCode", etSecurityCode.text.toString())
                intent.putExtra("address", etAddress.text.toString())
                intent.putExtra("city", etCity.text.toString())
                intent.putExtra("state", spinnerState.selectedItem.toString())
                intent.putExtra("zipCode", etZipCode.text.toString())
                intent.putExtra("country", spinnerCountry.selectedItem.toString())
                startActivity(intent)
            }
        }
    }
    private fun validateFields(): Boolean {
        if (spinnerCardType.selectedItemPosition == 0) {
            showToast("Please select a card type")
            return false
        }
        if (etCardNumber.text.length != 16) {
            showToast("Enter a valid 16-digit card number")
            return false
        }
        if (!etExpiryDate.text.matches(Regex("^(0[1-9]|1[0-2])/[0-9]{2}$"))) {
            showToast("Enter expiry in MM/YY format")
            return false
        }
        if (etSecurityCode.text.length != 3) {
            showToast("Enter a valid 3-digit security code")
            return false
        }
        if (etAddress.text.isEmpty()) {
            showToast("Enter your address")
            return false
        }
        if (etCity.text.isEmpty()) {
            showToast("Enter your city")
            return false
        }
        if (spinnerState.selectedItemPosition == 0) {
            showToast("Select your state")
            return false
        }
        if (etZipCode.text.length < 5) {
            showToast("Enter a valid ZIP Code")
            return false
        }
        if (spinnerCountry.selectedItemPosition == 0) {
            showToast("Select your country")
            return false
        }
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}