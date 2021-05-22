package org.teamseven.ols.dao

import androidx.room.*
import org.teamseven.ols.entities.User

@Dao
interface UserDao {
    @Query("select * from users")
    suspend fun getAll(): List<User>

    @Query("select * from users where id in (:userIds)")
    suspend fun findById(userIds: List<Int>): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}