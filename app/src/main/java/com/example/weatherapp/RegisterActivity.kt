package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = UserDbHelper(this)
        val etUsername = findViewById<EditText>(R.id.etRegUsername)
        val etPassword = findViewById<EditText>(R.id.etRegPassword)
        val etConfirm = findViewById<EditText>(R.id.etRegConfirm)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvBack = findViewById<TextView>(R.id.tvBackToLogin)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (db.register(username, password)) {
                Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "That username is already taken", Toast.LENGTH_SHORT).show()
            }
        }

        tvBack.setOnClickListener { finish() }
    }
}