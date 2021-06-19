package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User

@Entity(
    primaryKeys = ["user_id", "classroom_id"],
    tableName = "student_and_classroom_cross_ref"
)
data class StudentAndClassroomCrossRef(
    @ColumnInfo(name = "user_id")
    val studentId: Int,

    @ColumnInfo(name = "classroom_id")
    val classroomId: Int
)