package com.example.weatherapp

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class WeatherTask(
    private val textView: TextView,
    private val apiKey: String,
    private val units: String
) {

    fun fetchWeather(cityName: String) {
        textView.text = "Loading..."

        Thread {
            val output: String = try {
                val city = URLEncoder.encode(cityName.trim(), "UTF-8")
                val urlString =
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=$units"

                val connection = URL(urlString).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()
                    parseWeather(response)
                } else if (connection.responseCode == 404) {
                    "City not found. Check the spelling."
                } else {
                    "Error: ${connection.responseCode}"
                }
            } catch (e: Exception) {
                "Error: ${e.javaClass.simpleName} - ${e.message}"
            }

            // Updating the screen must happen on the main thread
            Handler(Looper.getMainLooper()).post {
                textView.text = output
            }
        }.start()
    }

    private fun parseWeather(response: String): String {
        val json = JSONObject(response)
        val city = json.getString("name")
        val main = json.getJSONObject("main")
        val weather = json.getJSONArray("weather").getJSONObject(0)
        val wind = json.getJSONObject("wind")

        val temperature = main.getDouble("temp")
        val humidity = main.getInt("humidity")
        val description = weather.getString("description")
        val speed = wind.getDouble("speed")

        val tempUnit = if (units == "imperial") "°F" else "°C"
        val windUnit = if (units == "imperial") "mph" else "m/s"

        return """
City: $city

Temperature: $temperature $tempUnit

Humidity: $humidity %

Description: $description

Wind Speed: $speed $windUnit
        """.trimIndent()
    }
}