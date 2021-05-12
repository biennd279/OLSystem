package org.teamseven.ols.api

import org.junit.Test
import org.junit.Assert.*
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import retrofit2.Call
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
}