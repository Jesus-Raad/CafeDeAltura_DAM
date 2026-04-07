package com.example.cafedealtura_dam.utils

import android.content.Context

public class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    public fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    public fun getUserName(): String {
        return prefs.getString("user_name", "Usuario") ?: "Usuario"
    }

    public fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun saveUserEmail(email: String) {
        prefs.edit().putString("user_email", email).apply()
    }

    fun getUserEmail(): String {
        return prefs.getString("user_email", "") ?: ""
    }
}