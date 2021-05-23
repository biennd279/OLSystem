package org.teamseven.ols.db

import androidx.room.*
import org.teamseven.ols.entities.Classroom

@Dao
interface ClassroomDao {
    @Query("select * from classrooms")
    suspend fun getAllClassroom(): List<Classroom>

    @Query("select * from classrooms where classroom_id = :classroomId")
    suspend fun findById(classroomId: Int): Classroom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg classroom: Classroom)

    @Delete
    suspend fun delete(classroom: Classroom)

    @Update
    suspend fun updateAll(vararg classroom: Classroom)
}