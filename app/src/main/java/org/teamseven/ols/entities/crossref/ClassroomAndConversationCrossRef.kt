package org.teamseven.ols.entities.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["classroom_id", "conversation_id"],
    tableName = "classroom_and_conversation_cross_ref"
)
data class ClassroomAndConversationCrossRef(
    @ColumnInfo(name = "classroom_id")
    val classroomId: Int,

    @ColumnInfo(name = "conversation_id")
    val conversationId: Int
)
