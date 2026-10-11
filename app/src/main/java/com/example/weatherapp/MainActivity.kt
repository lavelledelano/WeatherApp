package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var city: EditText
    private lateinit var result: TextView
    private var username = "guest"

    private val apiKey = "PASTE_YOUR_KEY_HERE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = intent.getStringExtra("username") ?: "guest"

        city = findViewById(R.id.etCity)
        result = findViewById(R.id.txtResult)
        val welcome = findViewById<TextView>(R.id.txtWelcome)
        val btnWeather = findViewById<Button>(R.id.btnWeather)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        welcome.text = "Welcome, $username"

        btnWeather.setOnClickListener {
            val cityName = city.text.toString()
            if (cityName.isBlank()) {
                result.text = "Please enter a city."
            } else {
                val prefs = getSharedPreferences("settings_$username", Context.MODE_PRIVATE)
                val units = prefs.getString("units", "metric") ?: "metric"
                WeatherTask(result, apiKey, units).fetchWeather(cityName)
            }
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Runs every time you come back to this screen, e.g. after saving settings
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("settings_$username", Context.MODE_PRIVATE)
        val defaultCity = prefs.getString("defaultCity", "") ?: ""
        if (city.text.isBlank() && defaultCity.isNotBlank()) {
            city.setText(defaultCity)
        }
    }
}