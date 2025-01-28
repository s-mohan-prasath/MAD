package com.legendmp.mad

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Exercise2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise2)
        var hungryBtn = findViewById<Button>(R.id.btn_eat)
        hungryBtn.setOnClickListener{
            var hungryImg = findViewById<ImageView>(R.id.image_hungry)
            val hungryText = findViewById<TextView>(R.id.text_hungry)
            if(hungryText.text.toString() == "DONE"){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else{
                hungryText.setText("I'm so full")
                hungryImg.setImageResource(R.drawable.emoji_happy)
                hungryBtn.setText("DONE")
            }
        }
    }
}