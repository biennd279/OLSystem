package org.teamseven.ols.entities.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.OwnerAndClassroomCrossRef
import org.teamseven.ols.entities.crossref.StudentAndClassroomCrossRef


data class ClassroomWithUser(
    @Embedded
    val classroom: Classroom,

    @Relation(
        parentColumn = "classroom_id",
        entityColumn = "user_id",
        associateBy = Junction(OwnerAndClassroomCrossRef::class)
    )
    val owner: User?,

    @Relation(
        parentColumn = "classroom_id",
        entityColumn = "user_id",
        associateBy = Junction(StudentAndClassroomCrossRef::class)
    )
    val students: List<User>?
)