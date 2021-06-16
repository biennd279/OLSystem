package org.teamseven.ols.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.ClassroomDao
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.ClassroomRepository
import org.teamseven.ols.repositories.UserRepository
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber

class ClassroomViewModel(context: Context): ViewModel() {

    private var authService: AuthService = AuthService.create(context)
    private var userService: UserService = UserService.create(context)
    private var classroomService: ClassroomService = ClassroomService.create(context)
    private var appDatabase: AppDatabase = AppDatabase.create(context)
    private var sessionManager: SessionManager = SessionManager(context)
    private var userRepository: UserRepository = UserRepository(
        userService,
        authService,
        appDatabase.userDao()
    )
    private var classroomRepository: ClassroomRepository = ClassroomRepository(
        classroomService,
        appDatabase.classroomDao(),
        appDatabase.userWithClassroomDao(),
        appDatabase.userDao()
    )

    val classOwner: LiveData<Resource<List<Classroom>>>
        get() = classroomRepository.getClassOwner(sessionManager.userId)
            .flowOn(Dispatchers.IO)
            .catch { Timber.i(it) }
            .asLiveData(viewModelScope.coroutineContext)

    val classJoined: LiveData<Resource<List<Classroom>>>
        get() = classroomRepository.getClassJoined(sessionManager.userId)
            .flowOn(Dispatchers.IO)
            .catch { Timber.i(it) }
            .asLiveData(viewModelScope.coroutineContext)
}