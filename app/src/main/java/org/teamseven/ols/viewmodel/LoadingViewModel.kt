package org.teamseven.ols.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class LoadingViewModel(context: Context) : ViewModel(){

    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var userDao: UserDao
    private lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    init {
        authService = AuthService.create(context)
        userService = UserService.create(context)
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = appDatabase.userDao()

        userRepository = UserRepository(
            userService,
            authService,
            userDao
        )
        sessionManager = SessionManager(context)
    }

    val currentUser = userRepository.getCurrentUser(sessionManager.userId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    val refreshToken = userRepository
        .validateAndRefreshToken(sessionManager.token)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)
}