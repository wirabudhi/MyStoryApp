package com.example.mystoryapp.data.model

import android.content.Context
import android.content.SharedPreferences

class StorySession(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun saveUser(loginResponse: LoginResponse) {
        preferences.edit().apply {
            putString(NAME, loginResponse.name)
            putString(TOKEN, loginResponse.token)
            putBoolean(LOGIN, loginResponse.isLogin)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = preferences.getBoolean(LOGIN, false)

    fun getToken(): String? = preferences.getString(TOKEN, null)

    companion object {
        private const val NAME = "name"
        private const val TOKEN = "token"
        private const val LOGIN = "login"
        private const val PREFS = "story_app_prefs"
    }
}
