package com.example.weatherapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDbHelper(context: Context) :
    SQLiteOpenHelper(context, "users.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "salt TEXT, " +
                    "passwordHash TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // Returns true if the user was created, false if the username is already taken
    fun register(username: String, password: String): Boolean {
        val salt = PasswordUtils.generateSalt()
        val values = ContentValues().apply {
            put("username", username.trim().lowercase())
            put("salt", salt)
            put("passwordHash", PasswordUtils.hash(password, salt))
        }
        return writableDatabase.insert("users", null, values) != -1L
    }

    // Returns true if the username exists and the password matches
    fun login(username: String, password: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT salt, passwordHash FROM users WHERE username = ?",
            arrayOf(username.trim().lowercase())
        )
        cursor.use {
            if (it.moveToFirst()) {
                val salt = it.getString(0)
                val storedHash = it.getString(1)
                return PasswordUtils.hash(password, salt) == storedHash
            }
        }
        return false
    }
}