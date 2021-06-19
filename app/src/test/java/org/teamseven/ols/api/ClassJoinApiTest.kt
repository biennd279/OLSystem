package org.teamseven.ols.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.*
import org.junit.runners.MethodSorters
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ClassJoinApiTest {
    companion object {

        lateinit var loginResponse: LoginResponse
        lateinit var classroomService: ClassroomService
        lateinit var lastClassroom: Classroom

        @BeforeClass
        @JvmStatic
        fun setup() = runBlocking {
            withContext(Dispatchers.IO) {
                val authService = AuthService.create()
                var loginRequest = LoginRequest(
                    email = "nagisa@gmail.com",
                    password = "password"
                )

                loginResponse = authService.login(loginRequest).body()!!
                classroomService = ClassroomService.create(loginResponse.token)

                val testClass = "Test Class"
                val testSchool = "Test School"
                val classroomInfoRequest = ClassroomInfoRequest(
                    name = testClass,
                    school = testSchool
                )

                lastClassroom = classroomService.createClassroom(classroomInfoRequest).body()!!

                loginRequest = LoginRequest(
                    email = "hiromi@gmail.com",
                    password = "password"
                )

                loginResponse = authService.login(loginRequest).body()!!
                classroomService = ClassroomService.create(loginResponse.token)
            }
        }
    }

    @Test
    fun firstJoinTest() = runBlocking {
        val response = classroomService.joinClass(lastClassroom.id)
        Assert.assertEquals(202, response.code())
    }

    @Test
    fun secondJoinViaCodeTest() = runBlocking {
        val response = classroomService.joinWithCode(lastClassroom.code)
        Assert.assertEquals(202, response.code())
    }

    @After
    fun thirdLeaveClass() = runBlocking {
        val response = classroomService.leaveClass(lastClassroom.id)
        Assert.assertEquals(202, response.code())
    }
}