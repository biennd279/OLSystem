package org.teamseven.ols.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.teamseven.ols.R
import timber.log.Timber

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

    @ExperimentalCoroutinesApi
    val flow : Flow<String?> = callbackFlow {
       val onTokenChange = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
           when(key) {
               USER_TOKEN -> offer(sharedPreferences.getString(key, null))
               USER_ID -> {}
               else -> {}
           }
       }

        prefs.registerOnSharedPreferenceChangeListener(onTokenChange)

        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(onTokenChange) }
    }
        .onStart { emit(this@SessionManager.token) }
        .catch { Timber.i(it) }
        .flowOn(Dispatchers.Default)
}