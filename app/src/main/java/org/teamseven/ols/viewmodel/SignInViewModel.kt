package org.teamseven.ols.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository

class SignInViewModel : ViewModel(){

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    fun runSignIn(loginRequest: LoginRequest, context: Context) {


    }
}