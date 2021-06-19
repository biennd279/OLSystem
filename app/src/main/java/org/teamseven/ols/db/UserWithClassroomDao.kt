package org.teamseven.ols.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.teamseven.ols.entities.crossref.OwnerAndClassroomCrossRef
import org.teamseven.ols.entities.crossref.StudentAndClassroomCrossRef
import org.teamseven.ols.entities.db.ClassroomWithUser
import org.teamseven.ols.entities.db.OwnerWithClassroom
import org.teamseven.ols.entities.db.StudentWithClassroom

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

    @Query("delete from classroom_and_conversation_cross_ref where classroom_id = :classroomId")
    suspend fun removeClassroomAndConversationCrossRef(classroomId: Int)

    @Query("delete from owner_and_classroom_cross_ref where classroom_id = :classroomId")
    suspend fun removeClassroomAndOwnerCrossRef(classroomId: Int)

    @Query("delete from student_and_classroom_cross_ref where classroom_id = :classroomId")
    suspend fun removeClassroomAndStudentCrossRef(classroomId: Int)

    @Transaction
    suspend fun removeClassroomCrossRef(classroomId: Int) {
        removeClassroomAndConversationCrossRef(classroomId)
        removeClassroomAndOwnerCrossRef(classroomId)
        removeClassroomAndStudentCrossRef(classroomId)
    }
}