package org.teamseven.ols.repositories

import com.auth0.android.jwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.requests.UpdatePasswordRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val authService: AuthService,
    private val userDao: UserDao,
) {
    fun getCurrentUser(currentUserId: Int): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, User>() {
            override fun query(): Flow<User> {
                return userDao.findById(currentUserId)
            }

            override fun shouldFetch(data: User?): Boolean {
                return currentUserId == 0 || data == null
            }

            override suspend fun fetch(): Response<User> {
                return userService.getProfile()
            }

            override fun processResponse(response: Response<User>): User {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: User) {
                userDao.insertAll(item)
            }
        }.asFlow()
    }

    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.loading(null))
            val response = authService.login(loginRequest)

            if (!response.isSuccessful || response.code() == 401) {
                emit(Resource.error(null, "Wrong credential..."))
            } else {
                emit(Resource.success(response.body()!!))
            }
        }
    }

    fun validateAndRefreshToken(token: String?): Flow<Resource<String?>> {
        return object : NetworkBoundResource<String?, LoginResponse>() {

            override fun isSuccessful(response: Response<LoginResponse>): Boolean {
                return response.code() == 200
            }

            override fun query(): Flow<String?> {
                return flow {
                    emit(token)
                }
            }

            override fun shouldFetch(data: String?): Boolean {
                return token == null || JWT(token).isExpired(Constants.TIME_EXPIRE)
            }

            override suspend fun fetch(): Response<LoginResponse> {
                return userService.refreshToken()
            }

            override fun processResponse(response: Response<LoginResponse>): String {
                return response.body()!!.token
            }

            override suspend fun saveCallResult(item: String?) {
                // Save in session manager over viewmodel/fragment, not here
            }

        }.asFlow()
    }

    fun updatePassword(updatePasswordRequest: UpdatePasswordRequest): Flow<Resource<Unit>> {
        return object : NetworkBoundResource<Unit, Void>() {

            override fun query(): Flow<Unit> {
                return flow {
                    emit(Unit)
                }
            }

            override fun shouldFetch(data: Unit?): Boolean = true

            override suspend fun fetch(): Response<Void> {
                return userService.updatePassword(updatePasswordRequest)
            }

            override fun processResponse(response: Response<Void>) {}

            override suspend fun saveCallResult(item: Unit) {}

        }.asFlow()
    }
}