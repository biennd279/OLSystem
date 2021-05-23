package org.teamseven.ols.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import okio.IOException
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.User
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUserDatabase {
    private lateinit var userDao: UserDao
    private lateinit var database: AppDatabase
    private lateinit var sampleUser: User

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = database.userDao()
        sampleUser = User(
            id = 1,
            name = "Nguyen Dinh Bien",
            email = "bien@mail.com",
            avatarUrl = "",
            role = ""
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun firstTestAddUser() = runBlocking {
        userDao.insertAll(sampleUser)
        Assert.assertTrue(listOf(sampleUser) == userDao.getAll().first())
    }

    @Test
    fun secondTestFindUser() = runBlocking {
        userDao.insertAll(sampleUser)
        Assert.assertEquals(userDao.findById(1).first(), sampleUser)
    }

    @Test
    fun secondTestRemoveUser(): Unit = runBlocking {
        userDao.insertAll(sampleUser)
        userDao.delete(sampleUser)
        val users = userDao.getAll().first()
        Assert.assertEquals(0, users.size)
    }
}