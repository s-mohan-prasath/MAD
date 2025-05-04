package com.legendmp.mad

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserDetailsActivity : AppCompatActivity() {

    lateinit var tvEmail:TextView;
    lateinit var tvUsername:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        tvEmail = findViewById(R.id.tvEmail)
        tvUsername = findViewById(R.id.tvUsername)

        val email = intent.getStringExtra("email")
        val username = intent.getStringExtra("username")

        tvEmail.text = email
        tvUsername.text = username
    }
}