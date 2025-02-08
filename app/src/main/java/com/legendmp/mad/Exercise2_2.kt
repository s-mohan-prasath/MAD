package com.legendmp.mad

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Exercise2_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise2_2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
// Initialize views
        val feedbackBtn: Button = findViewById(R.id.btn_ex2_2)
        val rating: RatingBar = findViewById(R.id.feedback_rating)
        val feedbackInput: EditText = findViewById(R.id.feedback_input)
        val feedbackTxt: TextView = findViewById(R.id.feedback_txt)

        // Set click listener for the button
        feedbackBtn.setOnClickListener {
            val feedbackText = feedbackInput.text.toString() // Get input text
            val ratingValue = rating.rating // Get rating value

            // Set the collected feedback to the TextView
            feedbackTxt.visibility = View.VISIBLE;
            feedbackTxt.text = "Rating: $ratingValue\nFeedback: $feedbackText"
        }
    }
}