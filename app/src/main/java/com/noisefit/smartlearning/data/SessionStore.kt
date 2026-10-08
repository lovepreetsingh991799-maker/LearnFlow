package com.noisefit.smartlearning.data

import android.content.Context

class SessionStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun markLoggedIn() {
        preferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "smart_learning_session"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}
