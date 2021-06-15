package org.teamseven.ols.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.teamseven.ols.db.utils.DateConverter
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.*

@Database(
    entities = [
        User::class,
        Classroom::class,
        Message::class,
        Conversation::class,
        ClassroomAndConversationCrossRef::class,
        ConversationAndMemberCrossRef::class,
        ConversationAndMessageCrossRef::class,
        CreatorAndConversationCrossRef::class,
        OwnerAndClassroomCrossRef::class,
        SenderAndMessageCrossRef::class,
        StudentAndClassroomCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun userWithClassroomDao(): UserWithClassroomDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }
    }
}