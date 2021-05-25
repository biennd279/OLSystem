package org.teamseven.ols.utils

import android.content.Context
import android.content.SharedPreferences
import org.teamseven.ols.R

class SessionManager constructor(context: Context) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    var token: String?
        get() = prefs.getString(USER_TOKEN, null)
        set(value) = prefs.edit().putString(USER_TOKEN, value).apply()

    var userId: Int
        get() = prefs.getInt(USER_ID, 0)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()

}