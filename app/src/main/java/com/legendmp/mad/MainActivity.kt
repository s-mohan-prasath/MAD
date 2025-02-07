package com.legendmp.mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var ex1Btn = findViewById<Button>(R.id.btn_ex1);
        var ex2Btn = findViewById<Button>(R.id.btn_ex1_1);
        var ex2_1Btn = findViewById<Button>(R.id.btn_ex2_1);
        var ex2_2Btn = findViewById<Button>(R.id.btn_ex2_2);
        var ex2_3Btn = findViewById<Button>(R.id.btn_ex2_3);

        val ex1Intent = Intent(this,Exercise1::class.java)
        val ex2Intent = Intent(this,Exercise2::class.java)
        val ex2_1Intent = Intent(this,Exercise2_1::class.java)
        val ex2_2Intent = Intent(this,Exercise2_2::class.java)
        val ex2_3Intent = Intent(this,Exercise2_3::class.java)
        ex1Btn.setOnClickListener{
            startActivity(ex1Intent)
        }
        ex2Btn.setOnClickListener{
            startActivity(ex2Intent)
        }
        ex2_1Btn.setOnClickListener{
            startActivity(ex2_1Intent)
        }
        ex2_2Btn.setOnClickListener{
            startActivity(ex2_2Intent)
        }
        ex2_3Btn.setOnClickListener{
            startActivity(ex2_3Intent)
        }
    }
}