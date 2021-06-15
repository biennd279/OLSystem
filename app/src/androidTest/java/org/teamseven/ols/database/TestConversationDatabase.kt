package org.teamseven.ols.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.ClassroomDao
import org.teamseven.ols.db.ConversationDao
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.ClassroomSetting
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.User

class TestConversationDatabase {
    lateinit var db: AppDatabase
    lateinit var userDao: UserDao
    lateinit var classroomDao: ClassroomDao
    lateinit var conversationDao: ConversationDao
    lateinit var sampleUser: User
    lateinit var sampleClassroom: Classroom
    lateinit var sampleConversation: Conversation

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = AppDatabase.create(context)
        userDao = db.userDao()
        classroomDao = db.classroomDao()
        conversationDao = db.conversationDao()

        sampleUser = User(
            id = 1,
            name = "Nguyen Dinh Bien",
            email = "bien@mail.com",
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

        sampleConversation = Conversation(
            id = 1,
            name = "test conversation",
            creatorId = sampleUser.id,
            classroomId = sampleClassroom.id,
            type = "group"
        )
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun testInsertConversation() = runBlocking {
        conversationDao.insert(sampleConversation)
        Assert.assertEquals(listOf(sampleConversation), conversationDao.getAllConversations().first())
        Assert.assertEquals(listOf(sampleConversation), conversationDao.getAllConversationsInClassroom(sampleClassroom.id).first())
    }

    @Test
    fun testDeleteConversation() = runBlocking {
        conversationDao.insert(sampleConversation)
        Assert.assertEquals(listOf(sampleConversation), conversationDao.getAllConversations().first())
        conversationDao.delete(sampleConversation)
        Assert.assertEquals(0, conversationDao.getAllConversations().first().size)
    }


}