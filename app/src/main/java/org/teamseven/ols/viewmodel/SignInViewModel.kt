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
import org.teamseven.ols.utils.SessionManager

class SignInViewModel(context: Context) : ViewModel(){

    private var authService: AuthService = AuthService.create(context)
    private var userService: UserService = UserService.create(context)
    private var appDatabase: AppDatabase = AppDatabase.create(context)
    private var userDao: UserDao = appDatabase.userDao()
    private var userRepository: UserRepository = UserRepository(
        userService,
        authService,
        userDao
    )


    fun signIn(loginRequest: LoginRequest) = userRepository.login(loginRequest)
}