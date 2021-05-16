package org.teamseven.ols.api

import org.junit.*
import org.junit.runners.MethodSorters
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import retrofit2.Call

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ClassJoinApiTest {
    companion object {

        lateinit var loginResponse: LoginResponse
        lateinit var classroomService: ClassroomService
        lateinit var lastClassroom : Classroom

        @BeforeClass
        @JvmStatic
        fun setup() {
            val authService = AuthService.create();
            var loginRequest = LoginRequest(
                email = "nagisa@gmail.com",
                password = "password"
            )
            val call : Call<LoginResponse> = authService.login(loginRequest)
            val response = call.execute()
            loginResponse = response.body()!!
            classroomService = ClassroomService.create(loginResponse.token)

            val testClass = "Test Class"
            val testSchool = "Test School"
            val classroomInfoRequest = ClassroomInfoRequest(
                name = testClass,
                school = testSchool
            )
            val newClassCall = classroomService.createClassroom(classroomInfoRequest)
            val newClassResponse = newClassCall.execute()
            lastClassroom = newClassResponse.body()!!

            loginRequest = LoginRequest(
                email = "hiromi@gmail.com",
                password = "password"
            )
            val newLoginCall : Call<LoginResponse> = authService.login(loginRequest)
            val newLoginResponse = newLoginCall.execute()
            loginResponse = newLoginResponse.body()!!
            classroomService = ClassroomService.create(loginResponse.token)
        }
    }

    @Test
    fun firstJoinTest() {
        val call = classroomService.joinClass(lastClassroom.id)
        val response = call.execute()
        Assert.assertEquals(202, response.code())
    }

    @Test
    fun secondJoinViaCodeTest() {
        val call = classroomService.joinWithCode(lastClassroom.code)
        val response = call.execute()
        Assert.assertEquals(202, response.code())
    }

    @After
    fun thirdLeaveClass() {
        val call = classroomService.leaveClass(lastClassroom.id)
        val response = call.execute()
        Assert.assertEquals(202, response.code())
    }
}