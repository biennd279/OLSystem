package org.teamseven.ols.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.network.AuthService
import java.lang.Exception

class AuthApiInstrumentTest {

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = ApplicationProvider.getApplicationContext()
        instrumentationContext.getSharedPreferences(
            "user_token",
            Context.MODE_PRIVATE
        )
    }

    @Test
    fun testLogin() = runBlocking {
            val authService = AuthService.create(instrumentationContext);
            val loginRequest = LoginRequest(
                email = "koross@gmail.com",
                password = "password"
            )

            try {
                val authResponse = authService.login(loginRequest).body()!!
                assertNotNull(authResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
}