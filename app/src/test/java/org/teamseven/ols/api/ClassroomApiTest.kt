package org.teamseven.ols.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.entities.responses.LoginResponse
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService

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
        lateinit var lastClassroom: Classroom

        @BeforeClass
        @JvmStatic
        fun setup() = runBlocking {
            withContext(Dispatchers.IO) {
                val authService = AuthService.create()
                val loginRequest = LoginRequest(
                    email = "koross@gmail.com",
                    password = "password"
                )

                loginResponse = authService.login(loginRequest).body()!!

                classroomService = ClassroomService.create(loginResponse.token)
            }

        }
    }

    @Test
    fun secondTestFetchAllClass() = runBlocking {
        val allClassroomsResponse = classroomService.fetchAllClassrooms().body()!!

        Assert.assertNotNull(allClassroomsResponse.listClassOwner)
        Assert.assertNotNull(allClassroomsResponse.listClassJoined)
    }

    @Test
    fun firstTestCreateClass() = runBlocking {
        val testClass = "Test Class"
        val testSchool = "Test School"

        val classroomInfoRequest = ClassroomInfoRequest(
            name = testClass,
            school = testSchool
        )

        val classroom = classroomService.createClassroom(classroomInfoRequest).body()!!

        Assert.assertNotNull(classroom)
        Assert.assertNotNull(classroom.setting)
        Assert.assertNotNull(classroom.code)
        Assert.assertEquals(testClass, classroom.name)
        Assert.assertEquals(testSchool, classroom.school)

        lastClassroom = classroom
    }


    @Test
    fun secondTestFetchClass() = runBlocking {
        val classroom = classroomService.fetchClassroom(lastClassroom.id).body()!!

        Assert.assertNotNull(classroom)

        Assert.assertNotNull(classroom.setting)
    }


    @Test
    fun secondTestUpdateClassroom() = runBlocking {
        val testClass = "Test Class 1"
        val testSchool = "Test School 1"

        val classroomInfoRequest = ClassroomInfoRequest(
            name = testClass,
            school = testSchool
        )

        val newClassroom = classroomService.update(lastClassroom.id, classroomInfoRequest)
            .body()!!

        Assert.assertEquals(newClassroom.name, testClass)
        Assert.assertEquals(newClassroom.school, testSchool)
    }

    @Test
    fun secondTestUpdateSetting() = runBlocking {
        val classroom = classroomService.fetchClassroom(lastClassroom.id).body()!!
        val setting = classroom.setting

        setting?.isRequiredApproval = 1
        setting?.typeParticipantMessage = "role-based"
        setting?.canMessageWithChildren = 0

        val newResponse = classroomService.updateSetting(lastClassroom.id, setting!!)
            .body()!!
        val newSetting = newResponse.setting
        Assert.assertNotNull("Setting null", newSetting)
        if (newSetting != null) {
            Assert.assertEquals(newSetting.isRequiredApproval, 1)
            Assert.assertEquals(newSetting.typeParticipantMessage, "role-based")
            Assert.assertEquals(newSetting.canMessageWithChildren, 0)

        }
    }

    @Test
    fun thirdTestDeleteClass() = runBlocking {
        val response = classroomService.deleteClass(lastClassroom.id)
        Assert.assertEquals(202, response.code())
    }

}