package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.Classroom

@Dao
interface ClassroomDao {
    @Query("select * from classrooms")
    fun getAllClassroom(): Flow<List<Classroom>>

    @Query("select * from classrooms where classroom_id = :classroomId")
    fun findById(classroomId: Int): Flow<Classroom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg classroom: Classroom)

    @Delete
    suspend fun delete(classroom: Classroom)

    @Update
    suspend fun updateAll(vararg classroom: Classroom)
}