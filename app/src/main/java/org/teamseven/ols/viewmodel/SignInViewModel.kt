package org.teamseven.ols.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import kotlinx.coroutines.launch
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository

class SignInViewModel(context: Context) : ViewModel(){

    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var userDao: UserDao
    private lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository

    init {
        authService = AuthService.create(context)
        userService = UserService.create(context)!!
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = appDatabase.userDao()

        userRepository = UserRepository(
            userService,
            authService,
            userDao
        )
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    fun signIn(loginRequest: LoginRequest) = userRepository.login(loginRequest)
}