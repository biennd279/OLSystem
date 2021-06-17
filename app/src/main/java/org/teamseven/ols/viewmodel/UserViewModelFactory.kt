package org.teamseven.ols.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService

class UserViewModelFactory(
    private val authService: AuthService,
    private val userService: UserService,
    private val appDatabase: AppDatabase,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                authService,
                userService,
                appDatabase,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}