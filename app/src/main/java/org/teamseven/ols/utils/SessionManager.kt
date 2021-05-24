package org.teamseven.ols.utils

import android.content.Context
import android.content.SharedPreferences
import org.teamseven.ols.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }


    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserId(id: Int) {
        val editor =  prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getCurrentUserId(): Int = prefs.getInt(USER_ID, 0)
}