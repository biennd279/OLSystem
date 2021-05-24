package org.teamseven.ols.entities.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User

data class OwnerWithClassroom(
    @Embedded
    val owner: User,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "classroom_id",
        associateBy = Junction(OwnerAndClassroomCrossRef::class)
    )
    val classrooms: List<Classroom>?
)