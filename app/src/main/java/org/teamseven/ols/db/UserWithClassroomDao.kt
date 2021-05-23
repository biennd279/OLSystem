package org.teamseven.ols.db

import androidx.room.*
import org.teamseven.ols.entities.db.*

@Dao
interface UserWithClassroomDao {
    @Transaction
    @Query("select * from users where user_id = :userId")
    suspend fun getAllClassroomOwn(userId: Int): OwnerWithClassroom

    @Transaction
    @Query("select * from users where user_id = :userId")
    suspend fun getAllClassJoined(userId: Int): StudentWithClassroom

    @Transaction
    @Query("select * from classrooms where classroom_id = :classroomId")
    suspend fun getMemberClassroom(classroomId: Int): ClassroomWithUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnerClassroom(ownerAndClassroomCrossRef: OwnerAndClassroomCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoinedClassroom(studentAndClassroomCrossRef: StudentAndClassroomCrossRef)

    @Delete
    suspend fun deleteOwner(ownerAndClassroomCrossRef: OwnerAndClassroomCrossRef)

    @Delete
    suspend fun deleteStudent(studentAndClassroomCrossRef: StudentAndClassroomCrossRef)
}