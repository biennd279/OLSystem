package org.teamseven.ols.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    fun testLogin() {
        val authService = AuthService.create(instrumentationContext);
        val loginRequest = LoginRequest(
            email = "koross@gmail.com",
            password = "password"
        )

        val call : Call<LoginResponse> = authService.login(loginRequest)

        try {
            val response = call.execute()
            val authResponse = response.body()
            assertNotNull(authResponse)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testLoginEnqueue() {
        val authService = AuthService.create(instrumentationContext);
        val loginRequest = LoginRequest(
            email = "koross@gmail.com",
            password = "password"
        )

        val call : Call<LoginResponse> = authService.login(loginRequest)

        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                assertNotNull(response)
                val authResponse = response.body()
                assertNotNull(authResponse)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                throw t
            }

        })
    }
}