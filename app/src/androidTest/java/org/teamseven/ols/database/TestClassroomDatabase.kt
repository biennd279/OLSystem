package org.teamseven.ols.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.ClassroomDao
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.ClassroomSetting

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestClassroomDatabase {
    private lateinit var database: AppDatabase
    private lateinit var classroomDao: ClassroomDao
    private lateinit var exampleClassroom: Classroom

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        classroomDao = database.classroomDao()
        val setting = ClassroomSetting (
            isRequiredApproval = 1,
            typeParticipantMessage = "role-base",
            canMessageWithChildren = 1
        )
        exampleClassroom = Classroom(
            id = 1,
            code = "abcdef",
            name = "Mobile",
            school = "University of Engineering and Technology",
            setting = setting
        )
    }

    @After
    @Throws(IOException::class)
    fun close() {
        database.close()
    }

    @Test
    fun firstTestCreateClassroom() = runBlocking {
        classroomDao.insertAll(exampleClassroom)
        Assert.assertTrue(listOf(exampleClassroom) == classroomDao.getAllClassroom().first())
    }

    @Test
    fun secondTestFindClassroom() = runBlocking {
        classroomDao.insertAll(exampleClassroom)
        Assert.assertEquals(classroomDao.findById(classroomId = exampleClassroom.id).first(),
            exampleClassroom)
    }

    @Test
    fun secondTestRemoveClasroom() = runBlocking {
        classroomDao.insertAll(exampleClassroom)
        Assert.assertEquals(classroomDao.getAllClassroom().first().size, 1)
        classroomDao.delete(exampleClassroom)
        Assert.assertEquals(classroomDao.getAllClassroom().first().size, 0)
    }
}