package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.User

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAll(): Flow<List<User>>

    @Query("select * from users where user_id = :userId")
    fun findById(userId: Int): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}