package org.teamseven.ols.entities.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.crossref.ClassroomAndConversationCrossRef

data class ClassroomWithConversation(
    @Embedded
    val classroom: Classroom,

    @Relation(
        parentColumn = "classroom_id",
        entityColumn = "conversation_id",
        associateBy = Junction(ClassroomAndConversationCrossRef::class)
    )
    val conversations: List<Conversation>
)
