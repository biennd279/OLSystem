package org.teamseven.ols.entities.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User

@Entity(
    primaryKeys = ["user_id", "classroom_id"],
)
data class OwnerAndClassroomCrossRef(
    @ColumnInfo(name = "user_id")
    val ownerId: Int,

    @ColumnInfo(name = "classroom_id")
    val classroomId: Int
) {
    constructor(user: User, classroom: Classroom): this(user.id, classroom.id)
}