package com.legendmp.mad

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(this)){
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivity(intent)
            }
        }

        var ex1Btn = findViewById<Button>(R.id.btn_ex1);
        var ex2Btn = findViewById<Button>(R.id.btn_ex1_1);
        var ex2_1Btn = findViewById<Button>(R.id.btn_ex2_1);
        var ex2_2Btn = findViewById<Button>(R.id.btn_ex2_2);
        var ex2_3Btn = findViewById<Button>(R.id.btn_ex2_3);
        var ex3Btn = findViewById<Button>(R.id.btn_ex3);
        var ex4Btn = findViewById<Button>(R.id.btn_ex4);
        var fileTransferBtn = findViewById<Button>(R.id.go_to_filetransfer);
        var openChatAppBtn = findViewById<Button>(R.id.openChatAppBtn);

        val ex1Intent = Intent(this,Exercise1::class.java)
        val ex2Intent = Intent(this,Exercise2::class.java)
        val ex2_1Intent = Intent(this,Exercise2_1::class.java)
        val ex2_2Intent = Intent(this,Exercise2_2::class.java)
        val ex2_3Intent = Intent(this,Exercise2_3::class.java)
        val ex3Intent = Intent(this,Exercise3::class.java)
        val chatIntent = Intent(this,ChatApp::class.java)

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
        ex3Btn.setOnClickListener{
            startActivity(ex3Intent)
        }
        ex4Btn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
        fileTransferBtn.setOnClickListener {
            startActivity(Intent(this,FileTransferActivity::class.java))
        }
        openChatAppBtn.setOnClickListener {
            startActivity(chatIntent)
        }
    }
}