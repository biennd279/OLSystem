package org.teamseven.ols.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception


class AuthApiTest {
    companion object {
        @BeforeClass
        fun enableLog() {
            Timber.plant(Timber.DebugTree())
        }
    }

    @Test
    fun testLogin() = runBlocking {
            val authService = AuthService.create();
            val loginRequest = LoginRequest(
                email = "koross@gmail.com",
                password = "password"
            )

            val authResponse = authService.login(loginRequest)

            try {
                assertNotNull(authResponse)
                Timber.d(authResponse.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    @Test
    fun testLoginEnqueue() = runBlocking {
            val authService = AuthService.create();
            val loginRequest = LoginRequest(
                email = "koross@gmail.com",
                password = "password"
            )

            val authResponse = authService.login(loginRequest)
            assertNotNull(authResponse)
    }
}