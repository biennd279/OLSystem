package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository
import org.teamseven.ols.utils.SessionManager

class UserViewModel(
    authService: AuthService,
    userService: UserService,
    appDatabase: AppDatabase,
    application: Application
): ViewModel() {
    private var userRepository = UserRepository(
        userService,
        authService,
        appDatabase.userDao()
    )

    private var sessionManager = SessionManager(application)

    val currentUser = userRepository.getCurrentUser(sessionManager.userId)
        .asLiveData(viewModelScope.coroutineContext)

    val validateToken = userRepository.validateAndRefreshToken(sessionManager.token)
}