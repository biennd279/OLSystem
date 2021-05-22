package org.teamseven.ols.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User

@Database(entities = [User::class, Classroom::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classroomDao(): ClassroomDao
}