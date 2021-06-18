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
import org.teamseven.ols.entities.User
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.ClassroomRepository
import org.teamseven.ols.repositories.UserRepository
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber

class ClassroomViewModel(
    private val classroomService: ClassroomService,
    private val appDatabase: AppDatabase,
    context: Context
): ViewModel() {

    private var sessionManager: SessionManager = SessionManager(context)

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

    fun students(classroomId: Int) = classroomRepository.getClassroomStudents(classroomId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun classroomInfo(classroomId: Int) = classroomRepository.getClassroomInfo(classroomId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun leaveClass(classroomId: Int) = classroomRepository.leaveClassroom(classroomId)
        .flowOn(Dispatchers.IO)
        .catch { Timber.i(it) }
        .asLiveData(viewModelScope.coroutineContext)
}