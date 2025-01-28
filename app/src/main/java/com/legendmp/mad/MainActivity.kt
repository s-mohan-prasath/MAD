package com.legendmp.mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var ex1Btn = findViewById<Button>(R.id.btn_ex1);
        var ex2Btn = findViewById<Button>(R.id.btn_ex2);

        val ex1Intent = Intent(this,Exercise1::class.java)
        val ex2Intent = Intent(this,Exercise2::class.java)
        ex1Btn.setOnClickListener{
            startActivity(ex1Intent)
        }
        ex2Btn.setOnClickListener{
            startActivity(ex2Intent)
        }
    }
}