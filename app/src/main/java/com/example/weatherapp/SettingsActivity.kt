package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val username = intent.getStringExtra("username") ?: "guest"
        val prefs = getSharedPreferences("settings_$username", Context.MODE_PRIVATE)

        val rbCelsius = findViewById<RadioButton>(R.id.rbCelsius)
        val rbFahrenheit = findViewById<RadioButton>(R.id.rbFahrenheit)
        val etDefaultCity = findViewById<EditText>(R.id.etDefaultCity)
        val btnSave = findViewById<Button>(R.id.btnSaveSettings)

        // Show the saved values (metric and empty city are the defaults)
        val units = prefs.getString("units", "metric")
        if (units == "imperial") rbFahrenheit.isChecked = true else rbCelsius.isChecked = true
        etDefaultCity.setText(prefs.getString("defaultCity", ""))

        btnSave.setOnClickListener {
            val chosenUnits = if (rbFahrenheit.isChecked) "imperial" else "metric"

            prefs.edit()
                .putString("units", chosenUnits)
                .putString("defaultCity", etDefaultCity.text.toString().trim())
                .apply()

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}