package com.legendmp.mad

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.legendmp.mad.helpers.SQLiteDatabaseHelper
import com.legendmp.mad.models.User

class SignupActivity : AppCompatActivity() {

    private lateinit var databaseHelper: SQLiteDatabaseHelper
    private lateinit var firebaseDb: DatabaseReference

    private lateinit var btnNext: Button;
    private lateinit var tilEmail: TextInputLayout;
    private lateinit var tilPassword: TextInputLayout;
    private lateinit var tilUsername: TextInputLayout;
    private lateinit var tilConfirmPassword: TextInputLayout;

    private val CHANNEL_ID = "signup_notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        tilEmail = findViewById(R.id.tilEmail)
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        // Initialize databases
        databaseHelper = SQLiteDatabaseHelper(this)
        firebaseDb = FirebaseDatabase.getInstance().reference.child("users")

        // Create notification channel
        createNotificationChannel()

        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            if (validateForm()) {
                saveUserData()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Email validation
        val email = tilEmail.editText?.text.toString().trim()
        if (email.isEmpty()) {
            tilEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Please enter a valid email"
            isValid = false
        } else {
            tilEmail.error = null
        }

        // Username validation
        val username = tilUsername.editText?.text.toString().trim()
        if (username.isEmpty()) {
            tilUsername.error = "Username is required"
            isValid = false
        } else if (username.length < 4) {
            tilUsername.error = "Username must be at least 4 characters"
            isValid = false
        } else {
            tilUsername.error = null
        }

        // Password validation
        val password = tilPassword.editText?.text.toString().trim()
        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            tilPassword.error = null
        }

        // Confirm password validation
        val confirmPassword = tilConfirmPassword.editText?.text.toString().trim()
        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (confirmPassword != password) {
            tilConfirmPassword.error = "Passwords don't match"
            isValid = false
        } else {
            tilConfirmPassword.error = null
        }

        return isValid
    }

    private fun saveUserData() {
        val email = tilEmail.editText?.text.toString().trim()
        val username = tilUsername.editText?.text.toString().trim()
        val password = tilPassword.editText?.text.toString().trim()

        // Save to SQLite
        val user = User(email = email, username = username, password = password)
        val sqliteId = databaseHelper.addUser(user)

        // Save to Firebase
        val firebaseId = firebaseDb.push().key
        if (firebaseId != null) {
            user.id = firebaseId
            firebaseDb.child(firebaseId).setValue(user)
        }

        // Show notification
        showNotification()

        // Move to next activity
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Account Created")
            .setContentText("Your details have been saved")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Signup Notifications"
            val descriptionText = "Notifications for account creation"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}