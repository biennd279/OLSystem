package org.teamseven.ols.repositories

import kotlinx.coroutines.flow.*
import org.teamseven.ols.db.ClassroomDao
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.db.UserWithClassroomDao
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.OwnerAndClassroomCrossRef
import org.teamseven.ols.entities.crossref.StudentAndClassroomCrossRef
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.responses.AllClassroomsResponse
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.utils.Resource
import retrofit2.Response
import javax.inject.Inject

class ClassroomRepository @Inject constructor(
    private val classroomService: ClassroomService,
    private val classroomDao: ClassroomDao,
    private val classroomUserDao: UserWithClassroomDao,
    private val userDao: UserDao
) {
    fun getClassOwner(userId: Int): Flow<Resource<List<Classroom>>> {
        return object: NetworkBoundResource<List<Classroom>, AllClassroomsResponse>() {
            override fun shouldFetch(data: List<Classroom>?): Boolean {
                return data.isNullOrEmpty()
            }

            override fun query(): Flow<List<Classroom>> {
                return classroomUserDao.getAllClassroomOwn(userId = userId)
                    .map {
                        it.classrooms
                    }
                    .filterNotNull()
            }

            override suspend fun fetch(): Response<AllClassroomsResponse> {
                return classroomService.fetchAllClassrooms()
            }

            override fun processResponse(response: Response<AllClassroomsResponse>): List<Classroom> {
                return response.body()!!.listClassOwner
            }

            override suspend fun saveCallResult(item: List<Classroom>) {
                classroomDao.insertAll(*item.toTypedArray())
                val crossRef = item.map {
                    OwnerAndClassroomCrossRef(
                        ownerId = userId,
                        classroomId = it.id
                    )
                }
                classroomUserDao.insertOwnerClassroom(*crossRef.toTypedArray())
            }
        }.asFlow()
    }

    fun getClassJoined(userId: Int): Flow<Resource<List<Classroom>>> {
        return object: NetworkBoundResource<List<Classroom>, AllClassroomsResponse>() {
            override fun shouldFetch(data: List<Classroom>?): Boolean {
                return data.isNullOrEmpty()
            }

            override fun query(): Flow<List<Classroom>> {
                return classroomUserDao.getAllClassroomOwn(userId = userId)
                    .map {
                        it.classrooms
                    }
                    .filterNotNull()
            }

            override suspend fun fetch(): Response<AllClassroomsResponse> {
                return classroomService.fetchAllClassrooms()
            }

            override fun processResponse(response: Response<AllClassroomsResponse>): List<Classroom> {
                return response.body()!!.listClassJoined
            }

            override suspend fun saveCallResult(item: List<Classroom>) {
                classroomDao.insertAll(*item.toTypedArray())
                val crossRef = item.map {
                    StudentAndClassroomCrossRef (
                        studentId = userId,
                        classroomId = it.id
                    )
                }
                classroomUserDao.insertJoinedClassroom(*crossRef.toTypedArray())
            }
        }.asFlow()
    }

    fun getClassroomInfo(classroomId: Int): Flow<Resource<Classroom>> {
        return object : NetworkBoundResource<Classroom, Classroom>() {
            override fun processResponse(response: Response<Classroom>): Classroom {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: Classroom) {
                classroomDao.insertAll(item)
            }

            override fun shouldFetch(data: Classroom?): Boolean {
                return data?.setting == null
            }

            override fun query(): Flow<Classroom> {
                return classroomDao.findById(classroomId = classroomId)
            }

            override suspend fun fetch(): Response<Classroom> {
                return classroomService.fetchClassroom(classroomId)
            }
        }.asFlow()
    }

    fun getClassroomStudents(classroomId: Int): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<User>>() {
            override fun processResponse(response: Response<List<User>>): List<User> {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: List<User>) {
                userDao.insertAll(*item.toTypedArray())
                val crossRefs = item.map {
                    StudentAndClassroomCrossRef(
                        studentId = it.id,
                        classroomId = classroomId
                    )
                }

                classroomUserDao.insertJoinedClassroom(*crossRefs.toTypedArray())
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun query(): Flow<List<User>> {
                return classroomUserDao.getMemberClassroom(classroomId = classroomId)
                    .map { it.students }
                    .filterNotNull()
            }

            override suspend fun fetch(): Response<List<User>> {
                return classroomService.fetchJoinedParticipant(classId = classroomId)
            }

        }.asFlow()
    }

    fun getClassroomTeacher(classroomId: Int): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, List<User>>() {
            override fun query(): Flow<User> {
                return classroomUserDao.getMemberClassroom(classroomId = classroomId)
                    .map { it.owner }
                    .filterNotNull()
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null
            }

            override suspend fun fetch(): Response<List<User>> {
                return classroomService.fetchClassOwner(classroomId)
            }

            override fun processResponse(response: Response<List<User>>): User {
                return response.body()!!.first()
            }

            override suspend fun saveCallResult(item: User) {
                userDao.insertAll(item)

                classroomUserDao.insertOwnerClassroom(
                    OwnerAndClassroomCrossRef(
                        ownerId = item.id,
                        classroomId = classroomId
                    )
                )
            }


        }.asFlow()
    }

    fun updateSetting() {

    }

    fun leaveClassroom(classroomId: Int) = flow {
        emit(Resource.loading(null))

        val response = classroomService.leaveClass(classId = classroomId)

        if (response.code() == 202) {
            emit(Resource.loading(classroomId))
            val classroom = classroomDao.findById(classroomId).first()
            classroomDao.delete(classroom)
            classroomUserDao.removeClassroomCrossRef(classroomId)
            emit(Resource.success(classroomId))
        } else {
            emit(Resource.error(null, response.body().toString()))
        }
    }

    fun joinClassroom(code: String) = flow {
        emit(Resource.loading(null))

        val response = classroomService.joinWithCode(code)

        if (response.code() == 202) {
            emit(Resource.loading(response.body()?.id))
            classroomDao.insertAll(response.body()!!)
            emit(Resource.success(response.body()?.id))
        } else {
            emit(Resource.error(null, response.body().toString()))
        }
    }

    fun createClassroom(classroomInfoRequest: ClassroomInfoRequest) = flow {
        emit(Resource.loading(null))

        val response = classroomService.createClassroom(
            classroomInfoRequest
        )

        if (response.code() == 201) {
            emit(Resource.loading(response.body()?.id))
            classroomDao.insertAll(response.body()!!)
            emit(Resource.success(response.body()?.id))
        } else {
            emit(Resource.error(null, response.body().toString()))
        }
    }
}