package org.teamseven.ols.db

import androidx.room.*
import org.teamseven.ols.entities.User

@Dao
interface UserDao {
    @Query("select * from users")
    suspend fun getAll(): List<User>

    @Query("select * from users where user_id = :userId")
    suspend fun findById(userId: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}