package org.teamseven.ols.api

import org.junit.*
import org.junit.runner.OrderWith
import org.junit.runners.MethodSorters
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import retrofit2.Call
import java.lang.Exception

/*
    Only test right behavior input
    Bad input we test in future, trust me =))
    I hope that
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ClassroomApiTest {

    companion object {

        lateinit var loginResponse: LoginResponse
        lateinit var classroomService: ClassroomService
        lateinit var lastClassroom : Classroom

        @BeforeClass
        @JvmStatic
        fun setup() {
            val authService = AuthService.create();
            val loginRequest = LoginRequest(
                email = "koross@gmail.com",
                password = "password"
            )

            val call : Call<LoginResponse> = authService.login(loginRequest)

            val response = call.execute()
            loginResponse = response.body()!!

            classroomService = ClassroomService.create(loginResponse.token)
        }
    }

    @Test
    fun secondTestFetchAllClass() {
        val call = classroomService.fetchAllClassrooms()

        val response = call.execute()
        Assert.assertNotNull(response)

        val allClassroomsResponse = response.body()
        Assert.assertNotNull(allClassroomsResponse)

        Assert.assertNotNull(allClassroomsResponse?.listClassOwner)
        Assert.assertNotNull(allClassroomsResponse?.listClassJoined)
    }

    @Test
    fun firstTestCreateClass() {
        val testClass = "Test Class"
        val testSchool = "Test School"

        val classroomInfoRequest = ClassroomInfoRequest(
            name = testClass,
            school = testSchool
        )

        val call = classroomService.createClassroom(classroomInfoRequest)

        val response = call.execute()

        Assert.assertNotNull(response)

        val classroom = response.body()

        Assert.assertNotNull(classroom)
        Assert.assertNotNull(classroom?.setting)
        Assert.assertNotNull(classroom?.code)
        Assert.assertEquals(testClass, classroom?.name)
        Assert.assertEquals(testSchool, classroom?.school)

        if (classroom != null) {
            lastClassroom = classroom
        }
    }


    @Test
    fun secondTestFetchClass() {
        val call = classroomService.fetchClassroom(lastClassroom.id)

        val response = call.execute()
        Assert.assertNotNull(response)

        val classroom = response.body()

        Assert.assertNotNull(classroom)

        Assert.assertNotNull(classroom?.setting)
    }


    @Test
    fun secondTestUpdateClassroom() {
        val testClass = "Test Class 1"
        val testSchool = "Test School 1"

        val classroomInfoRequest = ClassroomInfoRequest(
            name = testClass,
            school = testSchool
        )

        val updateCall = classroomService.update(lastClassroom.id, classroomInfoRequest)

        val newClassroom = updateCall.execute().body()

        Assert.assertEquals(newClassroom?.name, testClass)
        Assert.assertEquals(newClassroom?.school, testSchool)

    }

    @Test
    fun secondTestUpdateSetting() {
        val call = classroomService.fetchClassroom(lastClassroom.id)
        val classroom = call.execute().body()
        val setting = classroom?.setting

        setting?.isRequiredApproval = 1
        setting?.typeParticipantMessage = "role-based"
        setting?.canMessageWithChildren = 0

        val updateSettingCall = classroomService.updateSetting(lastClassroom.id, setting!!)
        val newResponse = updateSettingCall.execute().body()
        val newSetting = newResponse?.setting
        Assert.assertNotNull("Setting null", newSetting)
        Assert.assertEquals(newSetting?.isRequiredApproval, 1)
        Assert.assertEquals(newSetting?.typeParticipantMessage, "role-based")
        Assert.assertEquals(newSetting?.canMessageWithChildren, 0)
    }

    @Test
    fun thirdTestDeleteClass() {
        val call = classroomService.deleteClass(lastClassroom.id)
        val response = call.execute()

        Assert.assertEquals(202, response.code())
    }

}