package com.legendmp.mad

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Exercise1 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise1)

        var tempBtn = findViewById<Button>(R.id.btn_ex1_convert);
        var fahrenInput = findViewById<EditText>(R.id.input_fahren);
        var celciusText = findViewById<TextView>(R.id.text_celcius);

        tempBtn.setOnClickListener{
            val fahrenText = fahrenInput.text.toString()
            when{
            fahrenText.isEmpty()->{
                Toast.makeText(this,"Please Enter Value",Toast.LENGTH_SHORT).show()
        }
                else->{
                    val fahrenheit = fahrenText.toDouble()
                    val celcius = (fahrenheit-32)*5/9
                    celciusText.setText(String.format("%.2f C",celcius))
                    celciusText.visibility = android.view.View.VISIBLE;
                }
            }
        }
    }
}