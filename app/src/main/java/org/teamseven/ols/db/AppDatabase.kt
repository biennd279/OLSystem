package org.teamseven.ols.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.db.OwnerAndClassroomCrossRef
import org.teamseven.ols.entities.db.StudentAndClassroomCrossRef

@Database(
    entities = [
        User::class,
        Classroom::class,
        OwnerAndClassroomCrossRef::class,
        StudentAndClassroomCrossRef::class],

    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classroomDao(): ClassroomDao
    abstract fun userWithClassroomDao(): UserWithClassroomDao
}