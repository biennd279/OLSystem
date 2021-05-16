package org.teamseven.ols.api

import org.junit.Test
import org.junit.Assert.*
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class AuthApiTest {
    @Test
    fun testLogin() {
        val authService = AuthService.create();
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
        val authService = AuthService.create();
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