package org.teamseven.ols.dao

import androidx.room.*
import org.teamseven.ols.entities.Classroom

@Dao
interface ClassroomDao {
//    @Query("select * from classrooms where type_classroom = :type")
//    abstract suspend fun getAllClassroom(type: Int): List<Classroom>
//
//    suspend fun getClassroomOwn() = getAllClassroom(Classroom.TypeOfClass.Own.ordinal)
//    suspend fun getClassroomJoined() = getAllClassroom(Classroom.TypeOfClass.Joined.ordinal)

    @Query("select * from classrooms")
    abstract suspend fun getAllClassroom(): List<Classroom>

    @Query("select * from classrooms where id in (:classroomIds)")
    suspend fun findByIds(classroomIds: List<Int>): List<Classroom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg classroom: Classroom)

    @Delete
    suspend fun delete(classroom: Classroom)

    @Update
    suspend fun updateAll(vararg classroom: Classroom)
}