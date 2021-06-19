package org.teamseven.ols.entities.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.StudentAndClassroomCrossRef

data class StudentWithClassroom(
    @Embedded
    val student: User,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "classroom_id",
        associateBy = Junction(StudentAndClassroomCrossRef::class)
    )
    val classrooms: List<Classroom>?
)