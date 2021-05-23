package org.teamseven.ols.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["user_id", "classroom_id"],
)
data class StudentAndClassroomCrossRef(
    @ColumnInfo(name = "user_id")
    val studentId: Int,

    @ColumnInfo(name = "classroom_id")
    val classroomId: Int
)