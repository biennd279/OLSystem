package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.db.*

@Dao
interface UserWithClassroomDao {
    @Transaction
    @Query("select * from users where user_id = :userId")
    fun getAllClassroomOwn(userId: Int): Flow<OwnerWithClassroom>

    @Transaction
    @Query("select * from users where user_id = :userId")
    fun getAllClassJoined(userId: Int): Flow<StudentWithClassroom>

    @Transaction
    @Query("select * from classrooms where classroom_id = :classroomId")
    fun getMemberClassroom(classroomId: Int): Flow<ClassroomWithUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnerClassroom(vararg ownerAndClassroomCrossRef: OwnerAndClassroomCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoinedClassroom(vararg studentAndClassroomCrossRef: StudentAndClassroomCrossRef)

    @Delete
    suspend fun deleteOwner(ownerAndClassroomCrossRef: OwnerAndClassroomCrossRef)

    @Delete
    suspend fun deleteStudent(studentAndClassroomCrossRef: StudentAndClassroomCrossRef)
}