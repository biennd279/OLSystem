package org.teamseven.ols.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.ClassroomDao
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.db.UserWithClassroomDao
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.ClassroomSetting
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.db.OwnerAndClassroomCrossRef
import org.teamseven.ols.entities.db.StudentAndClassroomCrossRef

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUserClassroom {
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var classroomDao: ClassroomDao
    private lateinit var userWithClassroomDao: UserWithClassroomDao
    private lateinit var sampleClassroom: Classroom
    private lateinit var sampleUser: User
    private lateinit var sampleStudent: User

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = database.userDao()
        classroomDao = database.classroomDao()
        userWithClassroomDao = database.userWithClassroomDao()
        sampleUser = User(
            id = 1,
            name = "Nguyen Dinh Bien",
            email = "bien@mail.com",
            avatarUrl = "",
            role = ""
        )

        sampleStudent = User(
            id = 2,
            name = "biennd3",
            email = "biennd3@viettel.com.vn",
            avatarUrl = "",
            role = ""
        )

        val setting = ClassroomSetting(
            isRequiredApproval = 1,
            typeParticipantMessage = "role-base",
            canMessageWithChildren = 1
        )

        sampleClassroom = Classroom(
            id = 1,
            code = "abcdef",
            name = "Mobile",
            school = "University of Engineering and Technology",
            setting = setting
        )
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun firstTestInsertStudent() = runBlocking {
        userDao.insertAll(sampleStudent)
        classroomDao.insertAll(sampleClassroom)
        userWithClassroomDao.insertJoinedClassroom(
            StudentAndClassroomCrossRef(
                studentId = sampleStudent.id,
                classroomId = sampleClassroom.id
            )
        )

        val studentWithClassroom = userWithClassroomDao.getAllClassJoined(sampleStudent.id)
        val student = studentWithClassroom.student
        val classrooms = studentWithClassroom.classrooms

        Assert.assertEquals(student, sampleStudent)
        Assert.assertEquals(classrooms, listOf(sampleClassroom))
    }

    @Test
    fun firstTestInsertOwnClassroom() = runBlocking {
        userDao.insertAll(sampleUser)
        classroomDao.insertAll(sampleClassroom)

        userWithClassroomDao.insertOwnerClassroom(
            OwnerAndClassroomCrossRef(
                ownerId = sampleUser.id,
                classroomId = sampleClassroom.id
            )
        )

        val ownerWithClassroom = userWithClassroomDao.getAllClassroomOwn(sampleUser.id)
        val classrooms = ownerWithClassroom.classrooms
        val owner = ownerWithClassroom.owner

        Assert.assertEquals(sampleUser, owner)
        Assert.assertEquals(listOf(sampleClassroom), classrooms)
    }

    @Test
    fun secondTestClassroomUser() = runBlocking {
        userDao.insertAll(sampleUser)
        userDao.insertAll(sampleStudent)
        classroomDao.insertAll(sampleClassroom)

        userWithClassroomDao.insertJoinedClassroom(
            StudentAndClassroomCrossRef(
                studentId = sampleStudent.id,
                classroomId = sampleClassroom.id
            )
        )

        userWithClassroomDao.insertOwnerClassroom(
            OwnerAndClassroomCrossRef(
                ownerId = sampleUser.id,
                classroomId = sampleClassroom.id
            )
        )

        val classroomWithUser = userWithClassroomDao.getMemberClassroom(sampleClassroom.id)
        val classroom = classroomWithUser.classroom
        val students = classroomWithUser.students
        val owner = classroomWithUser.owner

        Assert.assertEquals(sampleClassroom, classroom)
        Assert.assertEquals(listOf(sampleStudent), students)
        Assert.assertEquals(sampleUser, owner)
    }
}